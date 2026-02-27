<template>
  <header class="flex items-center justify-between whitespace-nowrap border-b border-solid border-slate-200 dark:border-slate-800 bg-white dark:bg-slate-900 px-6 py-3 lg:px-40">
    <div class="flex items-center gap-8">
      <div class="flex items-center gap-4 text-primary">
        <div class="size-8 flex items-center justify-center rounded-lg bg-primary/10">
          <span class="material-symbols-outlined">rocket_launch</span>
        </div>
        <h2 class="text-slate-900 dark:text-white text-lg font-bold leading-tight tracking-tight">
          TechService
        </h2>
      </div>
    </div>
    
    <div class="flex flex-1 justify-end gap-6 items-center">
      <nav class="hidden md:flex items-center gap-8">
        <router-link 
          to="/admin/orders" 
          class="text-slate-600 dark:text-slate-300 text-sm font-medium hover:text-primary dark:hover:text-primary transition-colors"
          :class="{'text-primary border-b-2 border-primary py-1': $route.name === 'order-list'}"
        >
          Órdenes
        </router-link>
        <router-link 
          to="/admin/customers" 
          class="text-slate-600 dark:text-slate-300 text-sm font-medium hover:text-primary dark:hover:text-primary transition-colors"
          :class="{'text-primary border-b-2 border-primary py-1': $route.name === 'customer-list'}"
        >
          Clientes
        </router-link>
      </nav>
      
      <div class="h-8 w-px bg-slate-200 dark:bg-slate-800 hidden md:block"></div>
      
      <div class="flex items-center gap-4">
        <div 
          class="bg-center bg-no-repeat aspect-square bg-cover rounded-full size-9 border border-slate-200 dark:border-slate-700 cursor-pointer"
          :style="`background-image: url('${userAvatar}')`"
        ></div>
        <button 
          @click="logout"
          class="text-sm text-slate-600 dark:text-slate-300 hover:text-primary"
        >
          Cerrar sesión
        </button>
      </div>
    </div>
  </header>
</template>

<script setup>
import { useAuthStore } from '../../stores/auth.store'
import { useRouter } from 'vue-router'

const authStore = useAuthStore()
const router = useRouter()

const userAvatar = 'https://lh3.googleusercontent.com/aida-public/AB6AXuD5d2YcXsXXuuF8YQhvhV9YYSIUowgqEYzhltZdE-qqinAC2xYSTxeM9DHZ11Wh064g52VMumq5IhK4MP4EcwivtZ46I39o6b4ZjC6383AFScc4YJI6f7oC2EIajzDiaWeJubtpe7WbTj7gWPMT59jlq4yle2OTgd8Nzid2r1IkI3PZOgmDHIq6VQ6UbB4ARdMxDZQq5A6ouiSnHmcQHammcv3rvRup_yoW6XoZ0l8a-K9-bOtAELJzXef0ooYr_yGyowJCr2Ut4E4u'

function logout() {
  authStore.logout()
  router.push('/admin/login')
}
</script>