import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useOrderStore = defineStore('orders', () => {
  const orders = ref([
    {
      id: 'TK-1024',
      clientName: 'Alex Johnson',
      device: 'iPhone 13 Pro',
      date: 'Oct 24, 2023',
      status: 'in_process',
      email: 'alex@example.com',
      phone: '+1 (555) 123-4567',
      serialNumber: 'SN123456',
      problemDescription: 'Screen not responding to touch',
      priority: 'high',
      history: [
        { status: 'received', date: 'Oct 20, 09:15 AM' },
        { status: 'in_progress', date: 'Oct 21, 02:30 PM' }
      ]
    },
    // ... más órdenes
  ])
  
  const filters = ref({
    search: '',
    status: 'all'
  })
  
  const filteredOrders = computed(() => {
    return orders.value.filter(order => {
      const matchesSearch = filters.value.search === '' || 
        order.id.toLowerCase().includes(filters.value.search.toLowerCase()) ||
        order.clientName.toLowerCase().includes(filters.value.search.toLowerCase())
      
      const matchesStatus = filters.value.status === 'all' || 
        order.status === filters.value.status
      
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
    getOrderById 
  }
})