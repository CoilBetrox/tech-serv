<template>
  <div class="layout-container flex h-full grow flex-col">
    <Header />
    
    <main class="flex flex-1 justify-center py-8">
      <div class="layout-content-container flex flex-col max-w-[1200px] flex-1 px-4 lg:px-10">
        <!-- Header con título y botón -->
        <div class="flex flex-wrap items-end justify-between gap-4 mb-8">
          <div class="flex min-w-72 flex-col gap-2">
            <h1 class="text-slate-900 dark:text-white text-4xl font-black leading-tight tracking-tight">
              Gestión de servicios
            </h1>
            <p class="text-slate-500 dark:text-slate-400 text-base font-normal">
              Seguimiento y administración de órdenes de reparación.
            </p>
          </div>
          
          <router-link 
            to="/admin/orders/create"
            class="flex min-w-[140px] cursor-pointer items-center justify-center gap-2 overflow-hidden rounded-lg h-11 px-5 bg-primary text-white text-sm font-bold shadow-lg shadow-primary/20 hover:bg-primary/90 transition-all"
          >
            <span class="material-symbols-outlined text-[20px]">add</span>
            <span class="truncate">Crear ticket</span>
          </router-link>
        </div>
        
        <!-- Filtros y búsqueda -->
        <div class="bg-white dark:bg-slate-900 rounded-xl border border-slate-200 dark:border-slate-800 p-4 mb-6 shadow-sm">
          <div class="flex flex-col lg:flex-row justify-between gap-6">
            <div class="flex flex-1 max-w-md">
              <label class="relative w-full">
                <span class="absolute inset-y-0 left-0 flex items-center pl-3 text-slate-400">
                  <span class="material-symbols-outlined text-[20px]">search</span>
                </span>
                <input 
                  v-model="orderStore.filters.search"
                  class="block w-full pl-10 pr-3 py-2.5 bg-slate-50 dark:bg-slate-800 border-slate-200 dark:border-slate-700 rounded-lg text-sm placeholder:text-slate-400 focus:ring-primary focus:border-primary dark:text-white" 
                  placeholder="Buscar por ticket o nombre del cliente..." 
                  type="text"
                />
              </label>
            </div>
            
            <div class="flex items-center gap-3 overflow-x-auto pb-1 lg:pb-0">
              <span class="text-xs font-bold text-slate-400 uppercase tracking-wider mr-2 hidden sm:block">
                Filtrar por estado
              </span>
              
              <button 
                v-for="filterOption in filterOptions"
                :key="filterOption.value"
                @click="orderStore.filters.status = filterOption.value"
                class="flex h-9 shrink-0 items-center justify-center gap-x-2 rounded-lg px-4 text-sm font-medium transition-colors"
                :class="filterButtonClasses(filterOption.value)"
              >
                {{ filterOption.label }}
              </button>
              
              <div class="h-6 w-px bg-slate-200 dark:bg-slate-700 mx-2"></div>
              
              <button class="flex size-9 shrink-0 items-center justify-center rounded-lg bg-slate-100 dark:bg-slate-800 text-slate-600 dark:text-slate-300 hover:bg-slate-200 dark:hover:bg-slate-700">
                <span class="material-symbols-outlined text-[20px]">filter_list</span>
              </button>
            </div>
          </div>
        </div>
        
        <!-- Tabla de órdenes -->
        <div class="bg-white dark:bg-slate-900 rounded-xl border border-slate-200 dark:border-slate-800 overflow-hidden shadow-sm">
          <div class="overflow-x-auto">
            <table class="w-full text-left border-collapse">
              <thead>
                <tr class="bg-slate-50 dark:bg-slate-800/50 border-b border-slate-200 dark:border-slate-800">
                  <th class="px-6 py-4 text-xs font-bold text-slate-500 dark:text-slate-400 uppercase tracking-wider">Ticket</th>
                  <th class="px-6 py-4 text-xs font-bold text-slate-500 dark:text-slate-400 uppercase tracking-wider">Cliente</th>
                  <th class="px-6 py-4 text-xs font-bold text-slate-500 dark:text-slate-400 uppercase tracking-wider">Dispositivo</th>
                  <th class="px-6 py-4 text-xs font-bold text-slate-500 dark:text-slate-400 uppercase tracking-wider">Fecha</th>
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
                    <span class="font-mono font-bold text-primary">{{ order.id }}</span>
                  </td>
                  <td class="px-6 py-4 whitespace-nowrap">
                    <div class="flex items-center gap-3">
                      <div class="size-8 rounded-full bg-slate-100 dark:bg-slate-700 flex items-center justify-center text-xs font-bold text-slate-600 dark:text-slate-300">
                        {{ getInitials(order.clientName) }}
                      </div>
                      <span class="text-sm font-medium text-slate-900 dark:text-slate-200">
                        {{ order.clientName }}
                      </span>
                    </div>
                  </td>
                  <td class="px-6 py-4 whitespace-nowrap">
                    <span class="text-sm text-slate-600 dark:text-slate-400">{{ order.device }}</span>
                  </td>
                  <td class="px-6 py-4 whitespace-nowrap text-sm text-slate-600 dark:text-slate-400">
                    {{ order.date }}
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
          
          <!-- Paginación -->
          <div class="px-6 py-4 bg-slate-50 dark:bg-slate-800/50 border-t border-slate-200 dark:border-slate-800 flex items-center justify-between">
            <span class="text-sm text-slate-500 dark:text-slate-400 font-medium">
              Mostrando 1 a {{ orderStore.filteredOrders.length }} de {{ orderStore.filteredOrders.length }} tickets
            </span>
            <!-- Paginación aquí -->
          </div>
        </div>
      </div>
    </main>
    
    <Footer />
  </div>
</template>

<script setup>
import { onMounted } from 'vue'
import { useOrderStore } from '../../stores/order.store'
import { useUIStore } from '../../stores/ui.store'
import Header from '../../components/layout/Header.vue'
import Footer from '../../components/layout/Footer.vue'
import StatusBadge from '../../components/ui/StatusBadge.vue'

const orderStore = useOrderStore()
const uiStore = useUIStore()

onMounted(async () => {
  await orderStore.fetchOrders()
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

function openOrderModal(orderId) {
  // Implementar modal para vista rápida
  console.log('Open modal for order:', orderId)
}
</script>