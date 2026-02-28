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
            Consulta rápida de estado
          </h1>
          <p class="text-[#4c6c9a] dark:text-slate-400 text-lg font-normal leading-normal max-w-[600px] mx-auto">
            Consulta el estado de la reparación en tiempo real. Ingresa tu número de ticket para comenzar.
          </p>
        </div>
        
        <!-- Formulario de búsqueda -->
        <div class="w-full bg-white dark:bg-slate-900 rounded-xl shadow-sm border border-[#e7ecf3] dark:border-slate-800 p-8 mb-8">
          <form @submit.prevent="searchTicket" class="flex flex-col md:flex-row items-end gap-4">
            <div class="flex-1 w-full">
              <label class="flex flex-col w-full">
                <p class="text-[#0d131b] dark:text-slate-200 text-sm font-semibold leading-normal pb-2 uppercase tracking-wider">
                  Número de ticket
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
              <span class="truncate">{{ loading ? 'Buscando...' : 'Consultar estado' }}</span>
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
                <span class="text-xs text-[#4c6c9a] dark:text-slate-500 uppercase font-bold tracking-widest">Estado actual</span>
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
                <p class="text-xs font-bold text-[#4c6c9a] uppercase tracking-tighter">Última actividad</p>
                <p class="text-sm font-semibold dark:text-white">{{ ticketData.lastActivity }}</p>
              </div>
            </div>
          </div>
          
          <!-- Detalles adicionales -->
          <div class="mt-8 grid grid-cols-1 md:grid-cols-2 gap-6">
            <div class="flex items-start gap-3">
              <span class="material-symbols-outlined text-slate-400">phone</span>
              <div>
                <p class="text-xs text-slate-400">Contacto</p>
                <p class="text-sm font-medium dark:text-white">{{ ticketData.phone }}</p>
              </div>
            </div>
            
            <div class="flex items-start gap-3">
              <span class="material-symbols-outlined text-slate-400">description</span>
              <div>
                <p class="text-xs text-slate-400">{{ ticketData.deviceType === 'SERVICE' ? 'Servicio' : 'Falla reportada' }}</p>
                <p class="text-sm font-medium dark:text-white whitespace-pre-wrap break-words [overflow-wrap:anywhere] leading-relaxed">{{ ticketData.issue }}</p>
              </div>
            </div>
          </div>

          <!-- Historial de actualizaciones del técnico -->
          <div v-if="ticketData.updates && ticketData.updates.length" class="mt-8">
            <div class="flex items-center gap-2 mb-4">
              <span class="material-symbols-outlined text-primary">history</span>
              <h4 class="text-sm font-bold text-slate-900 dark:text-white uppercase tracking-wider">Actualizaciones del técnico</h4>
            </div>
            <div class="space-y-3">
              <div 
                v-for="(update, index) in ticketData.updates" 
                :key="index"
                class="p-4 rounded-lg border border-slate-200 dark:border-slate-700 bg-slate-50 dark:bg-slate-800/50"
              >
                <div class="flex items-start gap-3">
                  <div class="mt-0.5">
                    <div class="size-2 rounded-full bg-primary"></div>
                  </div>
                  <div class="flex-1 min-w-0">
                    <div class="flex items-center gap-2 mb-1">
                      <span class="text-xs font-bold text-primary uppercase">{{ update.stage }}</span>
                      <span v-if="update.date" class="text-xs text-slate-400">• {{ update.date }}</span>
                    </div>
                    <p class="text-sm text-slate-700 dark:text-slate-300 whitespace-pre-wrap break-words [overflow-wrap:anywhere] leading-relaxed">{{ update.message }}</p>
                  </div>
                </div>
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
import { ref } from 'vue'
import PublicHeader from '../../components/layout/PublicHeader.vue'
import Footer from '../../components/layout/Footer.vue'
import StatusStepper from '../../components/ui/StatusStepper.vue'
import { publicService } from '../../services/api'

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
  ticketData.value = null
  
  try {
    const data = await publicService.getTicketStatus(ticketNumber.value.trim().toUpperCase())
    const mappedStatus = normalizeStatus(data?.status)
    const deviceBrand = data?.device?.brand || ''
    const deviceModel = data?.device?.model || ''
    const deviceType = data?.device?.type || ''
    const deviceText = [deviceBrand, deviceModel || deviceType].filter(Boolean).join(' - ') || 'Dispositivo'
    const customerName = [data?.device?.customer?.firstName, data?.device?.customer?.lastName]
      .filter(Boolean)
      .join(' ') || 'Cliente'

    ticketData.value = {
      id: data?.ticketCode || ticketNumber.value.trim().toUpperCase(),
      clientName: customerName,
      deviceType: String(data?.device?.type || 'Device').toUpperCase(),
      device: deviceText,
      phone: data?.device?.customer?.phone || 'No disponible',
      issue: data?.description || 'Sin descripción de falla',
      status: mappedStatus,
      lastActivity: data?.createdAt
        ? `Creado el ${new Date(data.createdAt).toLocaleString('es-ES')}`
        : 'Sin actividad reciente',
      history: buildHistory(mappedStatus, data),
      updates: buildUpdates(mappedStatus, data)
    }
  } catch (err) {
    error.value = err?.message || 'Ocurrió un error. Inténtalo más tarde.'
    ticketData.value = null
  } finally {
    loading.value = false
  }
}

function normalizeStatus(status) {
  const normalized = (status || '').toUpperCase()
  const map = {
    RECIBIDO: 'received',
    EN_DIAGNOSTICO: 'in_progress',
    EN_REPARACION: 'in_progress',
    LISTO_PARA_ENTREGA: 'ready',
    ENTREGADO: 'delivered'
  }
  return map[normalized] || 'in_progress'
}

function buildHistory(status, data) {
  const steps = ['received', 'in_progress', 'ready', 'delivered']
  const activeIndex = steps.indexOf(status)
  if (activeIndex < 0) return []

  const datesByStep = {
    received: formatStepDate(data?.receivedAt || data?.entryDate),
    in_progress: formatStepDate(data?.inProgressAt),
    ready: formatStepDate(data?.finalizedAt),
    delivered: formatStepDate(data?.deliveredAt)
  }

  return steps.slice(0, activeIndex + 1).map(step => ({
    status: step,
    date: datesByStep[step] || ''
  }))
}

function formatStepDate(value) {
  if (!value) return ''
  if (typeof value === 'string' && /^\d{4}-\d{2}-\d{2}$/.test(value)) {
    const [year, month, day] = value.split('-')
    return `${day}/${month}/${year}`
  }
  const parsed = new Date(value)
  if (Number.isNaN(parsed.getTime())) return ''
  return parsed.toLocaleString('es-ES')
}

function buildUpdates(status, data) {
  const updates = []
  
  // Mensaje inicial (Recibido)
  if (data?.description) {
    updates.push({
      stage: 'Recibido',
      message: data.description,
      date: formatStepDate(data?.receivedAt || data?.entryDate)
    })
  }
  
  // Mensaje de en proceso (solo si ya pasó por este estado)
  const statusOrder = ['received', 'in_progress', 'ready', 'delivered']
  const currentIndex = statusOrder.indexOf(status)
  
  if (currentIndex >= 1 && data?.inProgressMessage) {
    updates.push({
      stage: 'En proceso',
      message: data.inProgressMessage,
      date: formatStepDate(data?.inProgressAt)
    })
  }
  
  // Mensaje de finalizado (solo si ya pasó por este estado)
  if (currentIndex >= 2 && data?.finalizedMessage) {
    updates.push({
      stage: 'Finalizado',
      message: data.finalizedMessage,
      date: formatStepDate(data?.finalizedAt)
    })
  }
  
  // Mensaje de entregado (solo si ya pasó por este estado)
  if (currentIndex >= 3 && data?.deliveredMessage) {
    updates.push({
      stage: 'Entregado',
      message: data.deliveredMessage,
      date: formatStepDate(data?.deliveredAt)
    })
  }
  
  return updates
}

// Utilidades
function formatStatus(status) {
  const statusMap = {
    'received': 'Recibido',
    'in_progress': 'En proceso',
    'ready': 'Finalizado',
    'delivered': 'Entregado'
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