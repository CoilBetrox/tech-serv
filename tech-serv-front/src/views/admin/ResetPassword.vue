<template>
  <div class="min-h-screen bg-slate-50 dark:bg-slate-950 flex items-center justify-center px-4">
    <div class="w-full max-w-md bg-white dark:bg-slate-900 rounded-xl border border-slate-200 dark:border-slate-800 shadow-lg p-8">
      <div class="text-center mb-6">
        <h1 class="text-2xl font-bold text-slate-900 dark:text-white">Restablecer contraseña</h1>
        <p class="text-sm text-slate-500 dark:text-slate-400 mt-2">
          Define tu nueva contraseña para continuar.
        </p>
      </div>

      <form class="space-y-4" @submit.prevent="handleSubmit">
        <div class="flex flex-col gap-2">
          <label class="text-sm font-semibold text-slate-800 dark:text-slate-200">Nueva contraseña</label>
          <input
            v-model="password"
            type="password"
            maxlength="120"
            required
            class="h-12 rounded-lg border border-slate-300 dark:border-slate-700 bg-white dark:bg-slate-800 px-4 text-slate-900 dark:text-white focus:ring-primary"
          />
        </div>

        <div class="flex flex-col gap-2">
          <label class="text-sm font-semibold text-slate-800 dark:text-slate-200">Confirmar contraseña</label>
          <input
            v-model="confirmPassword"
            type="password"
            maxlength="120"
            required
            class="h-12 rounded-lg border border-slate-300 dark:border-slate-700 bg-white dark:bg-slate-800 px-4 text-slate-900 dark:text-white focus:ring-primary"
          />
        </div>

        <p v-if="message" class="text-sm text-emerald-600 dark:text-emerald-400">{{ message }}</p>
        <p v-if="error" class="text-sm text-red-600 dark:text-red-400">{{ error }}</p>

        <button
          type="submit"
          :disabled="loading"
          class="w-full h-11 rounded-lg bg-primary text-white font-semibold hover:bg-primary/90 disabled:opacity-60"
        >
          {{ loading ? 'Guardando...' : 'Actualizar contraseña' }}
        </button>
      </form>

      <router-link
        to="/admin/login"
        class="mt-5 inline-flex text-sm font-semibold text-primary hover:underline"
      >
        Ir al login
      </router-link>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { authService } from '../../services/api'

const route = useRoute()
const router = useRouter()

const password = ref('')
const confirmPassword = ref('')
const loading = ref(false)
const message = ref('')
const error = ref('')

async function handleSubmit() {
  error.value = ''
  message.value = ''

  const token = String(route.query.token || '').trim()
  if (!token) {
    error.value = 'Token inválido o ausente.'
    return
  }

  if (!password.value || password.value.length < 6) {
    error.value = 'La contraseña debe tener al menos 6 caracteres.'
    return
  }

  if (password.value !== confirmPassword.value) {
    error.value = 'Las contraseñas no coinciden.'
    return
  }

  loading.value = true

  try {
    const response = await authService.resetPassword(token, password.value)
    message.value = response?.message || 'Contraseña actualizada correctamente.'

    setTimeout(() => {
      router.push('/admin/login')
    }, 1200)
  } catch (err) {
    error.value = err?.message || 'No se pudo restablecer la contraseña.'
  } finally {
    loading.value = false
  }
}
</script>
