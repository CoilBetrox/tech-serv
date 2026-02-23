<template>
  <div class="relative flex min-h-screen w-full flex-col overflow-x-hidden">
    <!-- Header público -->
    <PublicHeader />
    
    <!-- Elementos decorativos de fondo -->
    <div class="fixed top-0 right-0 -z-10 w-[500px] h-[500px] rounded-full blur-[100px] opacity-20 pointer-events-none"
         style="background: radial-gradient(circle, #136dec 0%, transparent 70%);"></div>
    <div class="fixed bottom-0 left-0 -z-10 w-[400px] h-[400px] rounded-full blur-[80px] opacity-10 pointer-events-none"
         style="background: radial-gradient(circle, #136dec 0%, transparent 70%);"></div>
    
    <main class="flex flex-1 flex-col items-center justify-start py-12 px-4 sm:px-10 lg:px-40">
      <div class="max-w-[800px] w-full flex flex-col items-center">
        <!-- Título -->
        <div class="w-full text-center mb-10">
          <h1 class="text-[#0d131b] dark:text-white tracking-light text-[36px] font-bold leading-tight pb-2">
            Quick Status Inquiry
          </h1>
          <p class="text-[#4c6c9a] dark:text-slate-400 text-lg font-normal leading-normal max-w-[600px] mx-auto">
            Track your device repair in real-time. Enter your ticket number below to get started.
          </p>
        </div>
        
        <!-- Formulario de búsqueda -->
        <div class="w-full bg-white dark:bg-slate-900 rounded-xl shadow-sm border border-[#e7ecf3] dark:border-slate-800 p-8 mb-8">
          <form @submit.prevent="searchTicket" class="flex flex-col md:flex-row items-end gap-4">
            <div class="flex-1 w-full">
              <label class="flex flex-col w-full">
                <p class="text-[#0d131b] dark:text-slate-200 text-sm font-semibold leading-normal pb-2 uppercase tracking-wider">
                  Ticket Number
                </p>
                <div class="relative group">
                  <span class="material-symbols-outlined absolute left-4 top-1/2 -translate-y-1/2 text-[#4c6c9a] dark:text-slate-500">
                    search
                  </span>
                  <input 
                    v-model="ticketNumber"
                    class="form-input flex w-full min-w-0 flex-1 resize-none overflow-hidden rounded-lg text-[#0d131b] dark:text-white focus:outline-0 focus:ring-2 focus:ring-primary border border-[#cfd9e7] dark:border-slate-700 bg-slate-50 dark:bg-slate-800 focus:border-primary h-14 placeholder:text-[#4c6c9a] pl-12 pr-4 text-base font-normal leading-normal transition-all" 
                    placeholder="e.g., TIC-12345" 
                    type="text"
                    required
                  />
                </div>
              </label>
            </div>
            
            <button 
              type="submit"
              class="flex min-w-[160px] cursor-pointer items-center justify-center overflow-hidden rounded-lg h-14 px-6 bg-primary text-white text-base font-bold leading-normal tracking-[0.015em] hover:bg-primary/90 transition-all shadow-lg shadow-primary/20 disabled:opacity-50 disabled:cursor-not-allowed"
              :disabled="loading || !ticketNumber"
            >
              <span v-if="loading" class="animate-spin material-symbols-outlined mr-2">hourglass_empty</span>
              <span class="truncate">{{ loading ? 'Searching...' : 'Check Status' }}</span>
            </button>
          </form>
          
          <!-- Mensaje de error -->
          <div v-if="error" class="mt-4 p-3 bg-red-50 dark:bg-red-900/20 border border-red-200 dark:border-red-800 rounded-lg">
            <p class="text-red-600 dark:text-red-400 text-sm flex items-center gap-2">
              <span class="material-symbols-outlined text-sm">error</span>
              {{ error }}
            </p>
          </div>
        </div>
        
        <!-- Resultados del ticket (solo se muestra cuando hay datos) -->
        <div v-if="ticketData" class="w-full bg-white dark:bg-slate-900 rounded-xl shadow-sm border border-[#e7ecf3] dark:border-slate-800 p-8 animate-fadeIn">
          <!-- Header del ticket -->
          <div class="flex flex-col sm:flex-row justify-between items-start sm:items-center mb-10 border-b border-slate-100 dark:border-slate-800 pb-6">
            <div>
              <div class="flex items-center gap-2 flex-wrap">
                <h3 class="text-xl font-bold dark:text-white">{{ ticketData.device }}</h3>
                <span class="bg-primary/10 text-primary text-xs font-bold px-2 py-1 rounded">{{ ticketData.id }}</span>
              </div>
              <p class="text-sm text-slate-500 dark:text-slate-400 mt-1">
                {{ ticketData.clientName }}
              </p>
            </div>
            
            <div class="mt-4 sm:mt-0 flex items-center gap-2">
              <div class="flex flex-col items-end">
                <span class="text-xs text-[#4c6c9a] dark:text-slate-500 uppercase font-bold tracking-widest">Current Status</span>
                <span class="text-primary font-bold">{{ formatStatus(ticketData.status) }}</span>
              </div>
              <div class="size-10 bg-primary/10 rounded-full flex items-center justify-center text-primary">
                <span class="material-symbols-outlined">{{ getStatusIcon(ticketData.status) }}</span>
              </div>
            </div>
          </div>
          
          <!-- Status Stepper Component -->
          <StatusStepper :status="ticketData.status" :history="ticketData.history" />
          
          <!-- Última actividad -->
          <div v-if="ticketData.lastActivity" class="mt-12 flex flex-col p-4 bg-slate-50 dark:bg-slate-800/50 rounded-lg">
            <div class="flex items-start gap-3">
              <span class="material-symbols-outlined text-primary mt-0.5">update</span>
              <div>
                <p class="text-xs font-bold text-[#4c6c9a] uppercase tracking-tighter">Last Activity</p>
                <p class="text-sm font-semibold dark:text-white">{{ ticketData.lastActivity }}</p>
              </div>
            </div>
          </div>
          
          <!-- Detalles adicionales -->
          <div class="mt-8 grid grid-cols-1 md:grid-cols-2 gap-6">
            <div class="flex items-start gap-3">
              <span class="material-symbols-outlined text-slate-400">phone</span>
              <div>
                <p class="text-xs text-slate-400">Contact</p>
                <p class="text-sm font-medium dark:text-white">{{ ticketData.phone }}</p>
              </div>
            </div>
            
            <div class="flex items-start gap-3">
              <span class="material-symbols-outlined text-slate-400">description</span>
              <div>
                <p class="text-xs text-slate-400">Issue</p>
                <p class="text-sm font-medium dark:text-white">{{ ticketData.issue }}</p>
              </div>
            </div>
          </div>
        </div>
        
        <!-- Mensaje de ayuda (siempre visible) -->
        <div class="mt-8 flex flex-col items-center text-center">
          <p class="text-[#4c6c9a] text-sm">Need help or didn't find your ticket?</p>
          <div class="flex gap-4 mt-2">
            <a class="text-primary text-sm font-bold flex items-center gap-1 hover:underline underline-offset-4" href="#">
              <span class="material-symbols-outlined text-sm">call</span>
              Contact Shop
            </a>
            <a class="text-primary text-sm font-bold flex items-center gap-1 hover:underline underline-offset-4" href="#">
              <span class="material-symbols-outlined text-sm">help_outline</span>
              Support Center
            </a>
          </div>
        </div>
      </div>
    </main>
    
    <Footer />
  </div>
</template>

<script setup>
import { ref } from 'vue'
import PublicHeader from '../../components/layout/PublicHeader.vue'
import Footer from '../../components/layout/Footer.vue'
import StatusStepper from '../../components/ui/StatusStepper.vue'

// Estado
const ticketNumber = ref('')
const loading = ref(false)
const error = ref('')
const ticketData = ref(null)

// Función para buscar ticket
async function searchTicket() {
  if (!ticketNumber.value.trim()) return
  
  loading.value = true
  error.value = ''
  
  try {
    // Aquí irá la llamada real a tu backend
    // const response = await fetch(`/api/public/tickets/${ticketNumber.value}`)
    // const data = await response.json()
    
    // SIMULACIÓN: Datos de ejemplo
    await new Promise(resolve => setTimeout(resolve, 1000))
    
    if (ticketNumber.value.toUpperCase() === 'TIC-98721') {
      ticketData.value = {
        id: 'TIC-98721',
        clientName: 'Alex Johnson',
        device: 'MacBook Pro 16" - Screen Repair',
        phone: '+1 (555) 123-4567',
        issue: 'Screen flickering and battery not charging',
        status: 'in_progress',
        lastActivity: 'Replaced display ribbon cable. Running diagnostics.',
        history: [
          { status: 'received', date: 'Oct 20, 09:15 AM' },
          { status: 'in_progress', date: 'Oct 21, 02:30 PM' },
          { status: 'ready', date: 'Oct 21, 02:30 PM' }
        ]
      }
      error.value = ''
    } else {
      // Simular ticket no encontrado
      ticketData.value = null
      error.value = `Ticket ${ticketNumber.value} not found. Please check the number and try again.`
    }
  } catch (err) {
    error.value = 'An error occurred. Please try again later.'
    ticketData.value = null
  } finally {
    loading.value = false
  }
}

// Utilidades
function formatStatus(status) {
  const statusMap = {
    'received': 'Received',
    'in_progress': 'In Progress',
    'ready': 'Ready for Pickup',
    'delivered': 'Delivered'
  }
  return statusMap[status] || status
}

function getStatusIcon(status) {
  const iconMap = {
    'received': 'check',
    'in_progress': 'settings_suggest',
    'ready': 'inventory_2',
    'delivered': 'handshake'
  }
  return iconMap[status] || 'help'
}
</script>

<style scoped>
@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.animate-fadeIn {
  animation: fadeIn 0.5s ease-out;
}
</style>