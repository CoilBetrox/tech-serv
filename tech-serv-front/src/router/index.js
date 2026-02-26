import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '../stores/auth.store'

// Vistas públicas
import TicketStatus from '../views/public/TicketStatus.vue'
import NotFound from '../views/NotFound.vue'

// Vistas de admin
import Login from '../views/admin/Login.vue'
import OrderList from '../views/admin/OrderList.vue'
import CreateOrder from '../views/admin/CreateOrder.vue'
import EditOrder from '../views/admin/EditOrder.vue'
import CustomerList from '../views/admin/CustomerList.vue'

const routes = [
  // Rutas públicas
  {
    path: '/',
    name: 'ticket-status',
    component: TicketStatus
  },
  
  // Rutas de admin
  {
    path: '/admin/login',
    name: 'admin-login',
    component: Login,
    meta: { public: true }
  },
  {
    path: '/admin/orders',
    name: 'order-list',
    component: OrderList,
    meta: { requiresAuth: true }
  },
  {
    path: '/admin/orders/create',
    name: 'create-order',
    component: CreateOrder,
    meta: { requiresAuth: true }
  },
  {
    path: '/admin/orders/:id',
    name: 'edit-order',
    component: EditOrder,
    meta: { requiresAuth: true }
  },
  {
    path: '/admin/customers',
    name: 'customer-list',
    component: CustomerList,
    meta: { requiresAuth: true }
  },
  
  // Redirecciones
  {
    path: '/admin',
    redirect: '/admin/orders'
  },

  // Ruta 404
  {
    path: '/:pathMatch(.*)*',
    name: 'not-found',
    component: NotFound
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// Guard de navegación
router.beforeEach((to, from, next) => {
  const authStore = useAuthStore()
  
  if (to.meta.requiresAuth && !authStore.isAuthenticated) {
    next('/admin/login')
  } else if (to.name === 'admin-login' && authStore.isAuthenticated) {
    next('/admin/orders')
  } else {
    next()
  }
})

export default router