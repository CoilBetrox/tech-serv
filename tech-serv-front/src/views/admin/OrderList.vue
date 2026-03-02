<template>
  <div class="layout-container flex h-full grow flex-col">
    <Header />
    
    <main class="flex flex-1 justify-center py-8">
      <div class="layout-content-container flex flex-col max-w-[1200px] flex-1 px-4 lg:px-10">
        <!-- Header con título y botón -->
        <div class="flex flex-col sm:flex-row flex-wrap items-start sm:items-end justify-between gap-4 mb-8">
          <div class="flex flex-col gap-2">
            <h1 class="text-slate-900 dark:text-white text-3xl sm:text-4xl font-black leading-tight tracking-tight">
              Gestión de servicios
            </h1>
            <p class="text-slate-500 dark:text-slate-400 text-sm sm:text-base font-normal">
              Seguimiento y administración de órdenes de reparación.
            </p>
          </div>
          
          <router-link 
            to="/admin/orders/create"
            class="flex w-full sm:w-auto min-w-[140px] cursor-pointer items-center justify-center gap-2 overflow-hidden rounded-lg h-11 px-5 bg-primary text-white text-sm font-bold shadow-lg shadow-primary/20 hover:bg-primary/90 transition-all"
          >
            <span class="material-symbols-outlined text-[20px]">add</span>
            <span class="truncate">Crear ticket</span>
          </router-link>
        </div>
        
        <!-- Filtros y búsqueda -->
        <div class="bg-white dark:bg-slate-900 rounded-xl border border-slate-200 dark:border-slate-800 p-3 sm:p-4 mb-6 shadow-sm">
          <div class="flex flex-col gap-4">
            <div class="flex flex-1 w-full">
              <label class="relative w-full">
                <span class="absolute inset-y-0 left-0 flex items-center pl-3 text-slate-400">
                  <span class="material-symbols-outlined text-[20px]">search</span>
                </span>
                <input 
                  v-model="orderStore.filters.search"
                  class="block w-full pl-10 pr-3 py-2.5 bg-slate-50 dark:bg-slate-800 border border-slate-200 dark:border-slate-700 rounded-lg text-sm placeholder:text-slate-400 focus:ring-primary focus:border-primary dark:text-white" 
                  placeholder="Buscar por ticket o nombre..." 
                  type="text"
                />
              </label>
            </div>
            
            <div class="flex flex-col sm:flex-row items-start sm:items-center gap-3 overflow-x-auto pb-1 sm:pb-0">
              <span class="text-xs font-bold text-slate-400 uppercase tracking-wider hidden sm:block">
                Filtrar por estado
              </span>
              
              <div class="flex flex-wrap gap-2 w-full sm:w-auto">
                <button 
                  v-for="filterOption in filterOptions"
                  :key="filterOption.value"
                  @click="orderStore.filters.status = filterOption.value"
                  class="flex h-9 shrink-0 items-center justify-center gap-x-2 rounded-lg px-3 sm:px-4 text-xs sm:text-sm font-medium transition-colors whitespace-nowrap"
                  :class="filterButtonClasses(filterOption.value)"
                >
                  {{ filterOption.label }}
                </button>
              </div>
              
            </div>
          </div>
        </div>
        <!-- Tabla de órdenes (Desktop) y Tarjetas (Mobile) -->
        <div class="hidden md:block bg-white dark:bg-slate-900 rounded-xl border border-slate-200 dark:border-slate-800 overflow-hidden shadow-sm">
          <div class="overflow-x-auto">
            <table class="w-full text-left border-collapse">
              <thead>
                <tr class="bg-slate-50 dark:bg-slate-800/50 border-b border-slate-200 dark:border-slate-800">
                  <th class="px-6 py-4 text-xs font-bold text-slate-500 dark:text-slate-400 uppercase tracking-wider">Ticket</th>
                  <th class="w-56 px-6 py-4 text-xs font-bold text-slate-500 dark:text-slate-400 uppercase tracking-wider">Cliente</th>
                  <th class="w-56 px-6 py-4 text-xs font-bold text-slate-500 dark:text-slate-400 uppercase tracking-wider">Requerimiento</th>
                  <th class="px-6 py-4 text-xs font-bold text-slate-500 dark:text-slate-400 uppercase tracking-wider">Fecha</th>
                  <th class="px-6 py-4 text-xs font-bold text-slate-500 dark:text-slate-400 uppercase tracking-wider">Costo estimado</th>
                  <th class="px-6 py-4 text-xs font-bold text-slate-500 dark:text-slate-400 uppercase tracking-wider">Estado</th>
                  <th class="px-6 py-4 text-xs font-bold text-slate-500 dark:text-slate-400 uppercase tracking-wider text-right">Acciones</th>
                </tr>
              </thead>
              <tbody class="divide-y divide-slate-100 dark:divide-slate-800">
                <tr 
                  v-for="order in orderStore.filteredOrders" 
                  :key="order.id"
                  class="hover:bg-slate-50 dark:hover:bg-slate-800/30 transition-colors group"
                >
                  <td class="px-6 py-4 whitespace-nowrap">
                    <span class="font-mono font-bold text-primary text-sm">{{ order.id }}</span>
                  </td>
                  <td class="w-56 px-6 py-4">
                    <div class="flex items-center gap-3">
                      <div class="size-8 rounded-full bg-slate-100 dark:bg-slate-700 flex items-center justify-center text-xs font-bold text-slate-600 dark:text-slate-300">
                        {{ getInitials(order.clientName) }}
                      </div>
                      <span class="text-sm font-medium text-slate-900 dark:text-slate-200">
                        {{ order.clientName }}
                      </span>
                    </div>
                  </td>
                  <td class="w-56 px-6 py-4 align-top">
                    <span class="text-sm text-slate-600 dark:text-slate-400 whitespace-normal break-words leading-relaxed">{{ order.device }}</span>
                  </td>
                  <td class="px-6 py-4 whitespace-nowrap text-sm text-slate-600 dark:text-slate-400">
                    {{ order.date }}
                  </td>
                  <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-slate-700 dark:text-slate-300">
                    {{ formatCurrency(order.estimatedCost) }}
                  </td>
                  <td class="px-6 py-4 whitespace-nowrap">
                    <StatusBadge :status="order.status" />
                  </td>
                  <td class="px-6 py-4 whitespace-nowrap text-right">
                    <div class="flex justify-end gap-2">
                      <button 
                        @click="openOrderModal(order.id)"
                        class="p-1.5 text-slate-400 hover:text-primary hover:bg-primary/10 rounded-md transition-all"
                      >
                        <span class="material-symbols-outlined text-[18px]">visibility</span>
                      </button>
                      <router-link 
                        :to="`/admin/orders/${order.id}`"
                        class="p-1.5 text-slate-400 hover:text-primary hover:bg-primary/10 rounded-md transition-all"
                      >
                        <span class="material-symbols-outlined text-[18px]">edit</span>
                      </router-link>
                    </div>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
          
          <!-- Paginación Desktop -->
          <div class="px-6 py-4 bg-slate-50 dark:bg-slate-800/50 border-t border-slate-200 dark:border-slate-800 flex items-center justify-between">
            <span class="text-sm text-slate-500 dark:text-slate-400 font-medium">
              Mostrando 1 a {{ orderStore.filteredOrders.length }} de {{ orderStore.filteredOrders.length }} tickets
            </span>
          </div>
        </div>

        <!-- Vista de tarjetas para Mobile (md:hidden) -->
        <div class="md:hidden space-y-3">
          <div 
            v-if="orderStore.filteredOrders.length === 0"
            class="bg-slate-50 dark:bg-slate-800/50 rounded-lg p-6 text-center"
          >
            <p class="text-slate-500 dark:text-slate-400 text-sm">No hay órdenes para mostrar</p>
          </div>

          <div 
            v-for="order in orderStore.filteredOrders" 
            :key="order.id"
            class="bg-white dark:bg-slate-900 rounded-lg border border-slate-200 dark:border-slate-800 p-4 shadow-sm hover:shadow-md transition-shadow"
          >
            <!-- Header de tarjeta -->
            <div class="flex items-start justify-between mb-3 pb-3 border-b border-slate-100 dark:border-slate-800">
              <div class="flex flex-col gap-1 flex-1 min-w-0">
                <span class="font-mono font-bold text-primary text-sm">{{ order.id }}</span>
                <div class="flex items-center gap-2 min-w-0">
                  <div class="size-6 rounded-full bg-slate-100 dark:bg-slate-700 flex items-center justify-center text-xs font-bold text-slate-600 dark:text-slate-300 flex-shrink-0">
                    {{ getInitials(order.clientName) }}
                  </div>
                  <span class="text-sm font-medium text-slate-900 dark:text-slate-200 truncate">
                    {{ order.clientName }}
                  </span>
                </div>
              </div>
              <div class="flex-shrink-0 ml-2">
                <StatusBadge :status="order.status" />
              </div>
            </div>

            <!-- Detalles de tarjeta -->
            <div class="space-y-2 mb-4">
              <div class="flex flex-col gap-0.5">
                <p class="text-xs text-slate-400 font-semibold uppercase tracking-wider">Requerimiento</p>
                <p class="text-sm text-slate-700 dark:text-slate-300">{{ order.device }}</p>
              </div>
              <div class="flex flex-col gap-0.5">
                <p class="text-xs text-slate-400 font-semibold uppercase tracking-wider">Fecha</p>
                <p class="text-sm text-slate-700 dark:text-slate-300">{{ order.date }}</p>
              </div>
              <div class="flex flex-col gap-0.5">
                <p class="text-xs text-slate-400 font-semibold uppercase tracking-wider">Costo estimado</p>
                <p class="text-sm text-slate-700 dark:text-slate-300">{{ formatCurrency(order.estimatedCost) }}</p>
              </div>
            </div>

            <!-- Acciones de tarjeta -->
            <div class="flex gap-2 pt-3 border-t border-slate-100 dark:border-slate-800">
              <button 
                @click="openOrderModal(order.id)"
                class="flex-1 flex items-center justify-center gap-2 h-9 rounded-lg bg-slate-100 dark:bg-slate-800 text-slate-700 dark:text-slate-300 hover:bg-primary hover:text-white transition-all text-sm font-medium"
              >
                <span class="material-symbols-outlined text-[16px]">visibility</span>
                Ver
              </button>
              <router-link 
                :to="`/admin/orders/${order.id}`"
                class="flex-1 flex items-center justify-center gap-2 h-9 rounded-lg bg-primary text-white hover:bg-primary/90 transition-all text-sm font-medium"
              >
                <span class="material-symbols-outlined text-[16px]">edit</span>
                Editar
              </router-link>
            </div>
          </div>
        </div>
      </div>

      <div
        v-if="isModalOpen && selectedOrder"
        class="fixed inset-0 z-50 flex items-center justify-center bg-slate-900/50 px-4"
        @click.self="closeOrderModal"
      >
        <div class="w-full max-w-md rounded-xl border border-slate-200 dark:border-slate-800 bg-white dark:bg-slate-900 shadow-xl">
          <div class="flex items-center justify-between border-b border-slate-200 dark:border-slate-800 px-5 py-4">
            <h3 class="text-lg font-bold text-slate-900 dark:text-white">Detalle de orden</h3>
            <button
              v-if="!isCreatedOrderPreview"
              type="button"
              class="rounded-md p-1 text-slate-500 hover:bg-slate-100 dark:hover:bg-slate-800 hover:text-slate-700 dark:hover:text-slate-200"
              @click="closeOrderModal"
              aria-label="Cerrar"
            >
              <span class="material-symbols-outlined text-[20px]">close</span>
            </button>
          </div>

          <div class="space-y-4 px-5 py-4">
            <div>
              <p class="text-xs font-semibold uppercase tracking-wider text-slate-400">Código de ticket</p>
              <p class="font-mono text-xl sm:text-2xl font-extrabold tracking-wide text-primary">{{ selectedOrder.id }}</p>
            </div>

            <div>
              <p class="text-xs font-semibold uppercase tracking-wider text-slate-400">Requerimiento</p>
              <p class="text-sm font-medium text-slate-800 dark:text-slate-200">{{ selectedOrder.device }}</p>
            </div>

            <div>
              <p class="text-xs font-semibold uppercase tracking-wider text-slate-400">Cliente</p>
              <p class="text-sm font-medium text-slate-800 dark:text-slate-200">{{ selectedOrder.clientName }}</p>
            </div>

            <div>
              <p class="text-xs font-semibold uppercase tracking-wider text-slate-400">Fecha</p>
              <p class="text-sm font-medium text-slate-800 dark:text-slate-200">{{ selectedOrder.date }}</p>
            </div>
          </div>

          <div v-if="!isCreatedOrderPreview" class="border-t border-slate-200 dark:border-slate-800 px-5 py-3 flex justify-end">
            <button
              type="button"
              class="h-10 rounded-lg px-4 text-sm font-semibold text-slate-700 dark:text-slate-300 border border-slate-300 dark:border-slate-700 hover:bg-slate-50 dark:hover:bg-slate-800"
              @click="closeOrderModal"
            >
              Cerrar
            </button>
          </div>
        </div>
      </div>
    </main>
    
    <Footer />
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useOrderStore } from '../../stores/order.store'
import { useUIStore } from '../../stores/ui.store'
import Header from '../../components/layout/Header.vue'
import Footer from '../../components/layout/Footer.vue'
import StatusBadge from '../../components/ui/StatusBadge.vue'

const route = useRoute()
const router = useRouter()
const orderStore = useOrderStore()
const uiStore = useUIStore()
const isModalOpen = ref(false)
const selectedOrder = ref(null)
const isCreatedOrderPreview = ref(false)

onMounted(async () => {
  await orderStore.fetchOrders()

  const createdTicket = route.query.createdTicket
  if (!createdTicket) return

  openOrderModal(String(createdTicket), true)

  const nextQuery = { ...route.query }
  delete nextQuery.createdTicket
  router.replace({ query: nextQuery })
})

const filterOptions = [
  { value: 'all', label: 'Todos' },
  { value: 'recibido', label: 'Recibido' },
  { value: 'en_proceso', label: 'En proceso' },
  { value: 'finalizado', label: 'Finalizado' },
  { value: 'entregado', label: 'Entregado' }
]

function filterButtonClasses(status) {
  if (orderStore.filters.status === status) {
    return 'bg-primary text-white font-semibold shadow-sm'
  }
  return 'bg-slate-100 dark:bg-slate-800 text-slate-600 dark:text-slate-300 hover:bg-slate-200 dark:hover:bg-slate-700'
}

function getInitials(name) {
  return name.split(' ').map(n => n[0]).join('').toUpperCase()
}

function openOrderModal(orderId, createdPreview = false) {
  const order = orderStore.orders.find(o => o.id === orderId)
  if (!order) return
  selectedOrder.value = order
  isCreatedOrderPreview.value = createdPreview
  isModalOpen.value = true
}

function closeOrderModal() {
  isModalOpen.value = false
  selectedOrder.value = null
  isCreatedOrderPreview.value = false
}

function formatCurrency(value) {
  const amount = Number(value)
  if (Number.isNaN(amount)) return '—'
  return new Intl.NumberFormat('es-EC', {
    style: 'currency',
    currency: 'USD'
  }).format(amount)
}
</script>