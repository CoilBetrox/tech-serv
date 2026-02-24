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

  function mapApiOrderToUi(apiOrder) {
    return {
      id: apiOrder.ticketCode,
      dbId: apiOrder.id,
      clientName: `${apiOrder.device.customer.firstName} ${apiOrder.device.customer.lastName}`,
      device: `${apiOrder.device.brand} ${apiOrder.device.model}`,
      date: new Date(apiOrder.createdAt).toLocaleDateString('en-US', { 
        month: 'short', day: 'numeric', year: 'numeric' 
      }),
      status: apiOrder.status.toLowerCase(),
      phone: apiOrder.device.customer.phone,
      description: apiOrder.description,
      estimatedCost: apiOrder.estimatedCost
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
  
  function createOrder(newOrder) {
    const order = {
      id: `TK-${1000 + orders.value.length + 1}`,
      date: new Date().toLocaleDateString('en-US', { month: 'short', day: 'numeric', year: 'numeric' }),
      status: 'pending',
      ...newOrder
    }
    orders.value.unshift(order)
    return order.id
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
    updateOrder,
    isLoading,
    fetchOrders,
    getOrderById: (id) => orders.value.find(o => o.id === id) 
  }
})