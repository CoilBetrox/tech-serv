<template>
  <div class="bg-background-light dark:bg-background-dark min-h-screen flex items-center justify-center p-4">
    <div class="fixed inset-0 z-0 overflow-hidden">
      <div class="w-full h-full bg-cover bg-center scale-105" style="background-image: url('https://lh3.googleusercontent.com/aida-public/AB6AXuDX874lgHRYjN04GWCvjghLgwtBWKKtRP1J5Gk3eAQAtDk7mAbd3N8NXKkbsMUWmIN5sg7MiaVaitglY9L8cikd0wborPBjorjW2PpKacvYT9SXHMZXJIzt97ys33ZA92g3IfLAioZrGpRV5prBRbQt0ez8MaCmxhET_1VdtEhQhyo3vAwVVNfrN8ggxg4VY8rrH_GK95s6YczdTCRY1t81keRiFErlejYzYdFIOFbfXMQqDZCekNlK1cOCDsQOAvspmJVGILqxuBYB');"></div>
      <div class="absolute inset-0 bg-slate-900/40 backdrop-blur-sm"></div>
    </div>
    
    <div class="relative z-10 w-full max-w-[450px]">
      <LoginForm @success="onLoginSuccess" />
      
      <p class="text-white/60 text-[10px] text-center mt-6 font-medium uppercase tracking-[2px]">
        © 2024 TechFix Manager v2.4.0
      </p>
    </div>
  </div>
</template>

<script setup>
import LoginForm from '../../components/auth/LoginForm.vue'
import { useAuthStore } from '../../stores/auth.store'
import { useRouter } from 'vue-router'

const authStore = useAuthStore()
const router = useRouter()

function onLoginSuccess() {
  router.push('/admin/orders')
}
</script>