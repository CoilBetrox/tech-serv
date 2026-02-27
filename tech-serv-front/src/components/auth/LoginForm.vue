<template>
  <div class="bg-white dark:bg-slate-900 shadow-2xl rounded-xl overflow-hidden">
    <div class="pt-8 pb-4 text-center">
      <div class="inline-flex items-center justify-center w-16 h-16 bg-primary/10 rounded-full mb-4">
        <span class="material-symbols-outlined text-primary text-4xl">build_circle</span>
      </div>
      <h1 class="text-[#0d131b] dark:text-white tracking-tight text-[28px] font-bold leading-tight px-4">
        TechService
      </h1>
      <p class="text-[#4c6c9a] dark:text-slate-400 text-sm font-medium mt-1">Portal de técnicos</p>
    </div>
    
    <form @submit.prevent="handleSubmit" class="px-8 pt-4 pb-6">
      <!-- Mensaje de error -->
      <div v-if="error" class="mb-4 p-3 bg-red-50 dark:bg-red-900/20 border border-red-200 dark:border-red-800 rounded-lg">
        <p class="text-red-600 dark:text-red-400 text-sm flex items-center gap-2">
          <span class="material-symbols-outlined text-sm">error</span>
          {{ error }}
        </p>
      </div>
      
      <div class="space-y-4">
        <div class="flex flex-col">
          <label class="flex flex-col flex-1">
            <p class="text-[#0d131b] dark:text-slate-200 text-sm font-medium leading-normal pb-1.5">
              Correo electrónico
            </p>
            <div class="relative">
              <span class="material-symbols-outlined absolute left-4 top-1/2 -translate-y-1/2 text-slate-400 text-[20px]">
                mail
              </span>
              <input 
                v-model="form.email"
                class="form-input flex w-full min-w-0 flex-1 resize-none overflow-hidden rounded-lg text-[#0d131b] dark:text-white focus:outline-0 focus:ring-2 focus:ring-primary/20 border border-[#cfd9e7] dark:border-slate-700 bg-white dark:bg-slate-800 focus:border-primary h-12 placeholder:text-[#4c6c9a] pl-11 pr-4 text-sm font-normal" 
                placeholder="Ingresa tu correo de trabajo" 
                type="email"
                required
              />
            </div>
          </label>
        </div>
        
        <div class="flex flex-col">
          <label class="flex flex-col flex-1">
            <div class="flex justify-between items-center pb-1.5">
              <p class="text-[#0d131b] dark:text-slate-200 text-sm font-medium leading-normal">Contraseña</p>
            </div>
            <div class="relative">
              <span class="material-symbols-outlined absolute left-4 top-1/2 -translate-y-1/2 text-slate-400 text-[20px]">
                lock
              </span>
              <input 
                v-model="form.password"
                class="form-input flex w-full min-w-0 flex-1 resize-none overflow-hidden rounded-lg text-[#0d131b] dark:text-white focus:outline-0 focus:ring-2 focus:ring-primary/20 border border-[#cfd9e7] dark:border-slate-700 bg-white dark:bg-slate-800 focus:border-primary h-12 placeholder:text-[#4c6c9a] pl-11 pr-4 text-sm font-normal" 
                placeholder="Ingresa tu contraseña" 
                type="password"
                required
              />
            </div>
          </label>
        </div>
      </div>
      
      <button 
        type="submit"
        class="w-full mt-8 bg-primary hover:bg-primary/90 text-white font-semibold h-12 rounded-lg transition-colors flex items-center justify-center gap-2 disabled:opacity-50 disabled:cursor-not-allowed"
        :disabled="loading"
      >
        <span v-if="loading" class="material-symbols-outlined text-[20px] animate-spin">hourglass_empty</span>
        <span v-else class="material-symbols-outlined text-[20px]">login</span>
        {{ loading ? 'Ingresando...' : 'Iniciar sesión' }}
      </button>
    </form>
    
    <div class="px-8 flex items-center gap-4">
      <div class="h-px bg-slate-200 dark:bg-slate-700 flex-1"></div>
      <span class="text-slate-400 text-xs font-medium uppercase tracking-wider">o</span>
      <div class="h-px bg-slate-200 dark:bg-slate-700 flex-1"></div>
    </div>
    
    <div class="p-8">
      <div class="bg-primary/5 dark:bg-primary/10 border border-primary/10 rounded-xl p-5 text-center">
        <p class="text-[#0d131b] dark:text-slate-200 text-sm font-semibold mb-2">¿Eres cliente?</p>
        <p class="text-[#4c6c9a] dark:text-slate-400 text-xs mb-4">Consulta el estado de tu reparación sin cuenta.</p>
        <router-link 
          to="/"
          class="inline-flex items-center gap-2 text-primary font-bold text-sm hover:underline"
        >
          <span class="material-symbols-outlined text-[18px]">search</span>
          Consulta rápida de ticket
        </router-link>
      </div>
    </div>
    
    <div class="bg-slate-50 dark:bg-slate-800/50 py-4 px-8 border-t border-slate-100 dark:border-slate-800 flex justify-between items-center text-[11px] text-[#4c6c9a] dark:text-slate-500 uppercase tracking-widest font-bold">
      <a class="hover:text-primary transition-colors" href="#">Solicitar acceso</a>
      <span class="text-slate-300 dark:text-slate-700">|</span>
      <router-link class="hover:text-primary transition-colors" to="/privacidad">Política de privacidad</router-link>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useAuthStore } from '../../stores/auth.store'
import { authService } from '../../services/api'
import { useRouter } from 'vue-router'

const emit = defineEmits(['success'])

const authStore = useAuthStore()
const router = useRouter()
const loading = ref(false)
const error = ref('')

const form = reactive({
  email: '',
  password: ''
})

async function handleSubmit() {
  loading.value = true
  error.value = ''
  
  try {
    // Llamar al servicio de autenticación
    const data = await authService.login(form.email, form.password)
    
    // Guardar el token real del backend
    authStore.setToken(data.token)
    
    // Emitir éxito para redirigir
    emit('success')
    
  } catch (err) {
    console.error('Login error:', err)
    error.value = err.message || 'Falló el inicio de sesión. Verifica tus credenciales.'
  } finally {
    loading.value = false
  }
}
</script>