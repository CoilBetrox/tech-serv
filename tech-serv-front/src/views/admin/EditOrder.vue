<template>
  <div class="relative flex h-auto min-h-screen w-full flex-col group/design-root overflow-x-hidden font-display">
    <Header />
    
    <main class="flex-1 flex flex-col items-center">
      <div class="max-w-[1200px] w-full px-4 md:px-10 py-8">
        <div class="flex flex-wrap items-center gap-2 text-sm mb-6">
          <router-link to="/admin/orders" class="text-slate-500 dark:text-slate-400 font-medium hover:text-primary transition-colors">
            Service Orders
          </router-link>
          <span class="text-slate-400 font-medium">/</span>
          <span class="text-slate-900 dark:text-white font-semibold">Edit Order #{{ orderId }}</span>
        </div>
        
        <div v-if="loading" class="flex justify-center py-12">
          <span class="material-symbols-outlined animate-spin text-primary">hourglass_empty</span>
        </div>
        
        <div v-else-if="order" class="bg-white dark:bg-slate-900 rounded-xl border border-slate-200 dark:border-slate-800 p-8">
          <h1 class="text-2xl font-bold mb-6">Edit Order</h1>
          <p class="text-slate-500">Vista de edición en construcción para orden: {{ orderId }}</p>
          
          <!-- Aquí irá el formulario de edición -->
          <pre class="mt-4 p-4 bg-slate-50 dark:bg-slate-800 rounded">{{ JSON.stringify(order, null, 2) }}</pre>
          
          <div class="flex justify-end gap-4 mt-8">
            <router-link to="/admin/orders" class="px-6 h-12 rounded-lg border border-slate-300 dark:border-slate-700 text-slate-700 dark:text-slate-300 font-bold hover:bg-slate-50 dark:hover:bg-slate-800 transition-colors inline-flex items-center">
              Back
            </router-link>
          </div>
        </div>
        
        <div v-else class="text-center py-12">
          <p class="text-slate-500">Order not found</p>
          <router-link to="/admin/orders" class="text-primary mt-4 inline-block">Return to orders</router-link>
        </div>
      </div>
    </main>
    
    <Footer />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { useOrderStore } from '../../stores/order.store'
import Header from '../../components/layout/Header.vue'
import Footer from '../../components/layout/Footer.vue'

const route = useRoute()
const orderStore = useOrderStore()
const orderId = route.params.id
const order = ref(null)
const loading = ref(true)

onMounted(() => {
  // Simular carga de datos
  setTimeout(() => {
    order.value = orderStore.getOrderById(orderId) || {
      id: orderId,
      clientName: 'Sample Client',
      device: 'Sample Device',
      status: 'pending'
    }
    loading.value = false
  }, 500)
})
</script>