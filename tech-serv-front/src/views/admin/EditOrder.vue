<template>
  <div class="relative flex h-auto min-h-screen w-full flex-col group/design-root overflow-x-hidden font-display">
    <Header />
    
    <main class="flex-1 flex flex-col items-center">
      <div class="max-w-[1200px] w-full px-4 md:px-10 py-8">
        <div class="flex flex-wrap items-center gap-2 text-sm mb-6">
          <router-link to="/admin/orders" class="text-slate-500 dark:text-slate-400 font-medium hover:text-primary transition-colors">
            Órdenes de servicio
          </router-link>
          <span class="text-slate-400 font-medium">/</span>
          <span class="text-slate-900 dark:text-white font-semibold">Editar orden #{{ orderId }}</span>
        </div>
        
        <div v-if="loading" class="flex justify-center py-12">
          <span class="material-symbols-outlined animate-spin text-primary">hourglass_empty</span>
        </div>
        
        <div v-else-if="order" class="bg-white dark:bg-slate-900 rounded-xl border border-slate-200 dark:border-slate-800 p-8 flex flex-col gap-8">
          <div class="flex flex-col gap-1">
            <h1 class="text-2xl font-bold">Editar orden</h1>
            <p class="text-slate-500">Actualiza el estado de la orden y guarda los cambios.</p>
          </div>

          <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div class="rounded-lg bg-slate-50 dark:bg-slate-800 p-4">
              <p class="text-xs uppercase text-slate-500">Ticket</p>
              <p class="font-semibold text-slate-900 dark:text-white">{{ order.id }}</p>
            </div>
            <div class="rounded-lg bg-slate-50 dark:bg-slate-800 p-4">
              <p class="text-xs uppercase text-slate-500">Cliente</p>
              <p class="font-semibold text-slate-900 dark:text-white">{{ order.clientName }}</p>
            </div>
            <div class="rounded-lg bg-slate-50 dark:bg-slate-800 p-4 md:col-span-2">
              <p class="text-xs uppercase text-slate-500">Dispositivo</p>
              <p class="font-semibold text-slate-900 dark:text-white">{{ order.device }}</p>
            </div>
          </div>

          <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div class="flex flex-col gap-2">
              <label class="text-sm font-semibold text-slate-900 dark:text-white">Estado</label>
              <select
                v-model="selectedStatus"
                :disabled="isDeliveredLocked"
                class="h-11 rounded-lg border border-slate-300 dark:border-slate-700 bg-white dark:bg-slate-800 px-3 text-slate-900 dark:text-white focus:ring-primary"
              >
                <option
                  v-for="option in statusOptions"
                  :key="option.value"
                  :value="option.value"
                  :disabled="!canSelectStatus(option.value)"
                >
                  {{ option.label }}
                </option>
              </select>
            </div>
            <div class="flex flex-col gap-2">
              <label class="text-sm font-semibold text-slate-900 dark:text-white">Mensaje (opcional)</label>
              <textarea
                v-model="statusMessage"
                :disabled="isDeliveredLocked"
                placeholder="Agrega un mensaje sobre este cambio de estado..."
                rows="2"
                maxlength="240"
                class="rounded-lg border border-slate-300 dark:border-slate-700 bg-white dark:bg-slate-800 px-3 py-2 text-slate-900 dark:text-white focus:ring-primary resize-none"
              ></textarea>
            </div>
          </div>

          <p v-if="isDeliveredLocked" class="text-sm text-amber-600 dark:text-amber-400">
            La orden ya está entregada. No se permite modificar estado ni mensajes.
          </p>

          <div class="grid grid-cols-1 md:grid-cols-2 gap-3 text-sm">
            <div class="rounded-lg border border-slate-200 dark:border-slate-700 p-3">
              <p class="text-slate-500">Recibido</p>
              <p class="font-medium text-slate-900 dark:text-white">{{ formatDate(order.receivedAt) }}</p>
            </div>
            <div class="rounded-lg border border-slate-200 dark:border-slate-700 p-3">
              <p class="text-slate-500">En proceso</p>
              <p class="font-medium text-slate-900 dark:text-white">{{ formatDate(order.inProgressAt) }}</p>
            </div>
            <div class="rounded-lg border border-slate-200 dark:border-slate-700 p-3">
              <p class="text-slate-500">Finalizado</p>
              <p class="font-medium text-slate-900 dark:text-white">{{ formatDate(order.finalizedAt) }}</p>
            </div>
            <div class="rounded-lg border border-slate-200 dark:border-slate-700 p-3">
              <p class="text-slate-500">Entregado</p>
              <p class="font-medium text-slate-900 dark:text-white">{{ formatDate(order.deliveredAt) }}</p>
            </div>
          </div>

          <p v-if="message" class="text-sm text-emerald-600 dark:text-emerald-400">{{ message }}</p>
          <p v-if="error" class="text-sm text-red-600 dark:text-red-400">{{ error }}</p>

          <div class="flex justify-end gap-4">
            <router-link to="/admin/orders" class="px-6 h-12 rounded-lg border border-slate-300 dark:border-slate-700 text-slate-700 dark:text-slate-300 font-bold hover:bg-slate-50 dark:hover:bg-slate-800 transition-colors inline-flex items-center">
              Volver
            </router-link>
            <button
              type="button"
              @click="saveChanges"
              :disabled="saving || isDeliveredLocked"
              class="px-6 h-12 rounded-lg bg-primary text-white font-bold hover:bg-primary/90 transition-colors disabled:opacity-60"
            >
              {{ saving ? 'Guardando...' : 'Guardar cambios' }}
            </button>
          </div>

          <div v-if="orderUpdates.length" class="pt-2">
            <div class="flex items-center gap-2 mb-4">
              <span class="material-symbols-outlined text-primary">history</span>
              <h4 class="text-sm font-bold text-slate-900 dark:text-white uppercase tracking-wider">Actualizaciones del técnico</h4>
            </div>
            <div class="space-y-3">
              <div
                v-for="(update, index) in orderUpdates"
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
        
        <div v-else class="text-center py-12">
          <p class="text-slate-500">Orden no encontrada</p>
          <router-link to="/admin/orders" class="text-primary mt-4 inline-block">Volver a órdenes</router-link>
        </div>
      </div>
    </main>
    
    <Footer />
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import { useOrderStore } from '../../stores/order.store'
import Header from '../../components/layout/Header.vue'
import Footer from '../../components/layout/Footer.vue'

const route = useRoute()
const orderStore = useOrderStore()
const orderId = route.params.id
const order = ref(null)
const loading = ref(true)
const saving = ref(false)
const error = ref('')
const message = ref('')
const selectedStatus = ref('RECIBIDO')
const statusMessage = ref('')

const statusOptions = [
  { value: 'RECIBIDO', label: 'Recibido' },
  { value: 'EN_PROCESO', label: 'En proceso' },
  { value: 'FINALIZADO', label: 'Finalizado' },
  { value: 'ENTREGADO', label: 'Entregado' }
]

onMounted(async () => {
  try {
    if (!orderStore.orders.length) {
      await orderStore.fetchOrders()
    }
    order.value = orderStore.getOrderById(orderId)
    if (order.value?.backendStatus) {
      selectedStatus.value = mapBackendToUiStatus(order.value.backendStatus)
    }
  } finally {
    loading.value = false
  }
})

async function saveChanges() {
  if (!order.value) return

  if (isDeliveredLocked.value) {
    error.value = 'La orden ya fue entregada y no se puede modificar.'
    return
  }

  if (!canSelectStatus(selectedStatus.value)) {
    error.value = 'No se puede regresar a un estado anterior.'
    return
  }

  saving.value = true
  error.value = ''
  message.value = ''

  try {
    const backendStatus = mapUiToBackendStatus(selectedStatus.value)
    const updated = await orderStore.updateOrderStatus(
      order.value.id, 
      backendStatus,
      statusMessage.value.trim() || null
    )
    order.value = updated
    statusMessage.value = '' // Limpiar mensaje después de guardar
    message.value = 'Estado actualizado correctamente. Ya puedes verificarlo en Consulta rápida por ticket.'
  } catch (err) {
    console.error('Error al actualizar estado:', err)
    error.value = err?.message || 'No se pudo actualizar el estado.'
  } finally {
    saving.value = false
  }
}

function formatDate(value) {
  if (!value) return '—'
  const parsed = new Date(value)
  if (Number.isNaN(parsed.getTime())) return '—'
  return parsed.toLocaleString('es-ES')
}

function mapUiToBackendStatus(uiStatus) {
  const map = {
    RECIBIDO: 'RECIBIDO',
    EN_PROCESO: 'EN_REPARACION',
    FINALIZADO: 'LISTO_PARA_ENTREGA',
    ENTREGADO: 'ENTREGADO'
  }
  return map[uiStatus] || 'RECIBIDO'
}

function mapBackendToUiStatus(backendStatus) {
  const normalized = (backendStatus || '').toUpperCase()
  const map = {
    RECIBIDO: 'RECIBIDO',
    EN_DIAGNOSTICO: 'EN_PROCESO',
    EN_REPARACION: 'EN_PROCESO',
    LISTO_PARA_ENTREGA: 'FINALIZADO',
    ENTREGADO: 'ENTREGADO'
  }
  return map[normalized] || 'RECIBIDO'
}

function statusRank(uiStatus) {
  const rankMap = {
    RECIBIDO: 0,
    EN_PROCESO: 1,
    FINALIZADO: 2,
    ENTREGADO: 3
  }
  return rankMap[uiStatus] ?? 0
}

const isDeliveredLocked = computed(() => {
  if (!order.value?.backendStatus) return false
  return mapBackendToUiStatus(order.value.backendStatus) === 'ENTREGADO'
})

const orderUpdates = computed(() => {
  if (!order.value) return []

  const updates = []
  const statusOrder = ['RECIBIDO', 'EN_PROCESO', 'FINALIZADO', 'ENTREGADO']
  const currentUiStatus = mapBackendToUiStatus(order.value.backendStatus)
  const currentIndex = statusOrder.indexOf(currentUiStatus)

  if (order.value.description) {
    updates.push({
      stage: 'Recibido',
      message: order.value.description,
      date: formatDate(order.value.receivedAt)
    })
  }

  if (currentIndex >= 1 && order.value.inProgressMessage) {
    updates.push({
      stage: 'En proceso',
      message: order.value.inProgressMessage,
      date: formatDate(order.value.inProgressAt)
    })
  }

  if (currentIndex >= 2 && order.value.finalizedMessage) {
    updates.push({
      stage: 'Finalizado',
      message: order.value.finalizedMessage,
      date: formatDate(order.value.finalizedAt)
    })
  }

  if (currentIndex >= 3 && order.value.deliveredMessage) {
    updates.push({
      stage: 'Entregado',
      message: order.value.deliveredMessage,
      date: formatDate(order.value.deliveredAt)
    })
  }

  return updates
})

function canSelectStatus(targetUiStatus) {
  if (!order.value?.backendStatus) return true
  if (isDeliveredLocked.value) return false
  const currentUiStatus = mapBackendToUiStatus(order.value.backendStatus)
  return statusRank(targetUiStatus) >= statusRank(currentUiStatus)
}
</script>