<template>
  <div class="relative flex h-auto min-h-screen w-full flex-col overflow-x-hidden font-display">
    <Header />

    <main class="flex-1 flex flex-col items-center">
      <div class="max-w-[1200px] w-full px-4 md:px-10 py-8 flex flex-col gap-6">
        <div class="flex flex-wrap items-center gap-2 text-sm">
          <router-link to="/admin/orders" class="text-slate-500 dark:text-slate-400 font-medium hover:text-primary transition-colors">
            Órdenes de servicio
          </router-link>
          <span class="text-slate-400 font-medium">/</span>
          <span class="text-slate-900 dark:text-white font-semibold">Clientes</span>
        </div>

        <div class="flex flex-col sm:flex-row items-start sm:items-end justify-between gap-4">
          <div class="flex flex-col gap-1">
            <h1 class="text-slate-900 dark:text-white text-3xl sm:text-4xl font-black leading-tight tracking-tight">
              Clientes asociados
            </h1>
            <p class="text-slate-500 dark:text-slate-400 text-sm sm:text-base">
              Clientes vinculados a tu cuenta de administrador.
            </p>
          </div>
        </div>

        <div v-if="loading" class="flex justify-center py-12">
          <span class="material-symbols-outlined animate-spin text-primary">hourglass_empty</span>
        </div>

        <div v-else class="bg-white dark:bg-slate-900 rounded-xl border border-slate-200 dark:border-slate-800 shadow-sm overflow-hidden">
          <div v-if="customers.length === 0" class="p-8 text-center">
            <p class="text-slate-500 dark:text-slate-400">No hay clientes asociados todavía.</p>
          </div>

          <div v-else>
            <div class="hidden md:block overflow-x-auto">
              <table class="w-full text-left border-collapse">
                <thead>
                  <tr class="bg-slate-50 dark:bg-slate-800/50 border-b border-slate-200 dark:border-slate-800">
                    <th class="px-6 py-4 text-xs font-bold text-slate-500 dark:text-slate-400 uppercase tracking-wider">Nombre</th>
                    <th class="px-6 py-4 text-xs font-bold text-slate-500 dark:text-slate-400 uppercase tracking-wider">Correo</th>
                    <th class="px-6 py-4 text-xs font-bold text-slate-500 dark:text-slate-400 uppercase tracking-wider">Teléfono</th>
                    <th class="px-6 py-4 text-xs font-bold text-slate-500 dark:text-slate-400 uppercase tracking-wider text-right">Acción</th>
                  </tr>
                </thead>
                <tbody class="divide-y divide-slate-100 dark:divide-slate-800">
                  <tr v-for="customer in customers" :key="customer.id" class="hover:bg-slate-50 dark:hover:bg-slate-800/30 transition-colors">
                    <td class="px-6 py-4 text-sm font-medium text-slate-900 dark:text-slate-200">
                      {{ customer.firstName }} {{ customer.lastName }}
                    </td>
                    <td class="px-6 py-4 text-sm text-slate-600 dark:text-slate-400">{{ customer.email || '—' }}</td>
                    <td class="px-6 py-4 text-sm text-slate-600 dark:text-slate-400">{{ customer.phone || '—' }}</td>
                    <td class="px-6 py-4 text-right">
                      <router-link
                        :to="`/admin/orders/create?customerId=${customer.id}`"
                        class="inline-flex items-center gap-2 h-9 px-4 rounded-lg bg-primary text-white text-sm font-semibold hover:bg-primary/90 transition-colors"
                      >
                        <span class="material-symbols-outlined text-[18px]">assignment_add</span>
                        Crear ticket
                      </router-link>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>

            <div class="md:hidden p-3 space-y-3">
              <div v-for="customer in customers" :key="customer.id" class="rounded-lg border border-slate-200 dark:border-slate-800 p-4">
                <p class="text-sm font-semibold text-slate-900 dark:text-slate-100">
                  {{ customer.firstName }} {{ customer.lastName }}
                </p>
                <p class="text-xs text-slate-500 mt-1">{{ customer.email || 'Sin correo' }}</p>
                <p class="text-xs text-slate-500">{{ customer.phone || 'Sin teléfono' }}</p>

                <router-link
                  :to="`/admin/orders/create?customerId=${customer.id}`"
                  class="mt-3 inline-flex w-full items-center justify-center gap-2 h-9 rounded-lg bg-primary text-white text-sm font-semibold hover:bg-primary/90 transition-colors"
                >
                  <span class="material-symbols-outlined text-[18px]">assignment_add</span>
                  Crear ticket
                </router-link>
              </div>
            </div>
          </div>
        </div>
      </div>
    </main>

    <Footer />
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { orderService } from '../../services/api'
import Header from '../../components/layout/Header.vue'
import Footer from '../../components/layout/Footer.vue'

const loading = ref(true)
const customers = ref([])

onMounted(async () => {
  try {
    const token = localStorage.getItem('token')
    customers.value = await orderService.getMyCustomers(token)
  } catch (error) {
    console.error('Error al cargar clientes:', error)
    customers.value = []
  } finally {
    loading.value = false
  }
})
</script>
