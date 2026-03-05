<template>
  <div class="min-h-screen bg-slate-50 dark:bg-slate-950 flex items-center justify-center px-4">
    <div class="w-full max-w-md bg-white dark:bg-slate-900 rounded-xl border border-slate-200 dark:border-slate-800 shadow-lg p-8">
      <div class="text-center mb-6">
        <h1 class="text-2xl font-bold text-slate-900 dark:text-white">Recuperar contraseña</h1>
        <p class="text-sm text-slate-500 dark:text-slate-400 mt-2">
          Ingresa tu correo y te enviaremos un enlace para restablecer tu contraseña.
        </p>
      </div>

      <form class="space-y-4" @submit.prevent="handleSubmit">
        <div class="flex flex-col gap-2">
          <label class="text-sm font-semibold text-slate-800 dark:text-slate-200">Correo</label>
          <input
            v-model="email"
            type="email"
            maxlength="40"
            required
            placeholder="admin@mail.com"
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
          {{ loading ? 'Enviando...' : 'Enviar enlace' }}
        </button>
      </form>

      <router-link
        to="/admin/login"
        class="mt-5 inline-flex text-sm font-semibold text-primary hover:underline"
      >
        Volver al login
      </router-link>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { authService } from '../../services/api'

const email = ref('')
const loading = ref(false)
const message = ref('')
const error = ref('')

async function handleSubmit() {
  loading.value = true
  message.value = ''
  error.value = ''

  try {
    const response = await authService.forgotPassword(email.value.trim())
    message.value = response?.message || 'Te enviamos un email de recuperación de contraseña a la dirección que nos has indicado.'
  } catch (err) {
    error.value = err?.message || 'No se pudo procesar la solicitud.'
  } finally {
    loading.value = false
  }
}
</script>
