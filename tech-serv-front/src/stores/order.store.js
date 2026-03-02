import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { orderService } from '../services/api'

export const useOrderStore = defineStore('orders', () => {
  const orders = ref([])
  const isLoading = ref(false)

  const filters = ref({
    search: '',
    status: 'all'
  })

  function mapBackendStatusToUi(status) {
    const normalized = (status || '').toUpperCase()
    const map = {
      RECIBIDO: 'recibido',
      EN_DIAGNOSTICO: 'en_proceso',
      EN_REPARACION: 'en_proceso',
      LISTO_PARA_ENTREGA: 'finalizado',
      ENTREGADO: 'entregado'
    }
    return map[normalized] || 'recibido'
  }

  function normalizeBackendStatus(status) {
    return (status || '').toUpperCase().trim()
  }

  function mapApiOrderToUi(apiOrder) {
    return {
      id: apiOrder.ticketCode,
      dbId: apiOrder.id,
      clientName: `${apiOrder.device.customer.firstName} ${apiOrder.device.customer.lastName}`,
      device: `${apiOrder.device.brand} ${apiOrder.device.model}`,
      date: new Date(apiOrder.createdAt).toLocaleDateString('es-ES', {
        month: 'short', day: 'numeric', year: 'numeric' 
      }),
      status: mapBackendStatusToUi(apiOrder.status),
      backendStatus: normalizeBackendStatus(apiOrder.status),
      phone: apiOrder.device.customer.phone,
      description: apiOrder.description,
      estimatedCost: apiOrder.estimatedCost,
      receivedAt: apiOrder.receivedAt,
      inProgressAt: apiOrder.inProgressAt,
      finalizedAt: apiOrder.finalizedAt,
      deliveredAt: apiOrder.deliveredAt,
      inProgressMessage: apiOrder.inProgressMessage,
      finalizedMessage: apiOrder.finalizedMessage,
      deliveredMessage: apiOrder.deliveredMessage,
      createdAt: apiOrder.createdAt
    }
  }

  async function fetchOrders() {
    isLoading.value = true
    try {
      const token = localStorage.getItem('token') 
      const data = await orderService.getAllOrders(token)
      
      orders.value = data.map(mapApiOrderToUi)
    } catch (error) {
      console.error('Error fetching orders:', error)
    } finally {
      isLoading.value = false
    }
  }
  
  const filteredOrders = computed(() => {
    return orders.value.filter(order => {
      const matchesSearch = filters.value.search === '' || 
        order.id.toLowerCase().includes(filters.value.search.toLowerCase()) ||
        order.clientName.toLowerCase().includes(filters.value.search.toLowerCase())
      
      const matchesStatus = filters.value.status === 'all' || 
        order.status === filters.value.status.toLowerCase()
      
      return matchesSearch && matchesStatus
    })
  })
  
  async function createOrder(payload) {
    const token = localStorage.getItem('token')
    const created = await orderService.createOrder(token, payload)
    const mapped = mapApiOrderToUi(created)
    orders.value.unshift(mapped)
    return mapped
  }

  async function updateOrderStatus(ticketId, backendStatus, message = null) {
    const order = orders.value.find(o => o.id === ticketId)
    if (!order) throw new Error('Orden no encontrada en memoria')

    const token = localStorage.getItem('token')
    const updated = await orderService.updateOrderStatus(token, order.dbId, backendStatus, message)
    const mapped = mapApiOrderToUi(updated)

    const index = orders.value.findIndex(o => o.id === ticketId)
    if (index !== -1) {
      orders.value[index] = mapped
    }

    return mapped
  }
  
  function updateOrder(id, updates) {
    const index = orders.value.findIndex(o => o.id === id)
    if (index !== -1) {
      orders.value[index] = { ...orders.value[index], ...updates }
    }
  }
  
  function getOrderById(id) {
    return orders.value.find(o => o.id === id)
  }
  
  return { 
    orders, 
    filters, 
    filteredOrders, 
    createOrder, 
    updateOrderStatus,
    updateOrder,
    isLoading,
    fetchOrders,
    getOrderById: (id) => orders.value.find(o => o.id === id) 
  }
})