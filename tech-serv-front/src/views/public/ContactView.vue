<template>
  <div class="relative flex min-h-screen w-full flex-col overflow-x-hidden">
    <component :is="isAdminContext ? Header : PublicHeader" />

    <main class="flex flex-1 flex-col items-center justify-start py-12 px-4 sm:px-10 lg:px-40">
      <div class="max-w-[800px] w-full">
        <div class="w-full text-center mb-10">
          <h1 class="text-[#0d131b] dark:text-white tracking-light text-[36px] font-bold leading-tight pb-2">
            Contáctanos
          </h1>
          <p class="text-[#4c6c9a] dark:text-slate-400 text-lg font-normal leading-normal max-w-[600px] mx-auto">
            Envíanos tu mensaje y te responderemos lo antes posible.
          </p>
        </div>

        <div class="w-full bg-white dark:bg-slate-900 rounded-xl shadow-sm border border-[#e7ecf3] dark:border-slate-800 p-8">
          <form @submit.prevent="handleSubmit" class="space-y-4">
            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div class="flex flex-col gap-2">
                <label class="text-[#0d131b] dark:text-slate-200 text-sm font-semibold">Nombre *</label>
                <input
                  v-model="form.name"
                  class="form-input w-full rounded-lg text-[#0d131b] dark:text-white focus:outline-0 focus:ring-2 focus:ring-primary border border-[#cfd9e7] dark:border-slate-700 bg-slate-50 dark:bg-slate-800 h-12 px-4 text-sm"
                  placeholder="Nombre" 
                  type="text"
                  maxlength="30"
                  required
                />
              </div>

              <div class="flex flex-col gap-2">
                <label class="text-[#0d131b] dark:text-slate-200 text-sm font-semibold">Correo *</label>
                <input
                  v-model="form.email"
                  class="form-input w-full rounded-lg text-[#0d131b] dark:text-white focus:outline-0 focus:ring-2 focus:ring-primary border border-[#cfd9e7] dark:border-slate-700 bg-slate-50 dark:bg-slate-800 h-12 px-4 text-sm"
                  placeholder="cliente@mail.com" 
                  type="email"
                  maxlength="40"
                  required
                />
              </div>
            </div>

            <div class="flex flex-col gap-2">
              <label class="text-[#0d131b] dark:text-slate-200 text-sm font-semibold">Asunto *</label>
              <input
                v-model="form.subject"
                class="form-input w-full rounded-lg text-[#0d131b] dark:text-white focus:outline-0 focus:ring-2 focus:ring-primary border border-[#cfd9e7] dark:border-slate-700 bg-slate-50 dark:bg-slate-800 h-12 px-4 text-sm"
                placeholder="Asunto del mensaje"
                type="text"
                maxlength="100"
                required
              />
            </div>

            <div class="flex flex-col gap-2">
              <label class="text-[#0d131b] dark:text-slate-200 text-sm font-semibold">Mensaje *</label>
              <textarea
                v-model="form.message"
                class="form-textarea w-full min-h-[160px] rounded-lg text-[#0d131b] dark:text-white focus:outline-0 focus:ring-2 focus:ring-primary border border-[#cfd9e7] dark:border-slate-700 bg-slate-50 dark:bg-slate-800 p-4 text-sm"
                placeholder="Escribe tu mensaje aquí..."
                maxlength="240"
                required
              ></textarea>
            </div>

            <p v-if="error" class="text-sm text-red-600 dark:text-red-400">{{ error }}</p>

            <div class="flex justify-end">
              <button
                type="submit"
                :disabled="loading"
                class="flex min-w-[160px] cursor-pointer items-center justify-center overflow-hidden rounded-lg h-12 px-6 bg-primary text-white text-base font-bold leading-normal tracking-[0.015em] hover:bg-primary/90 transition-all disabled:opacity-50 disabled:cursor-not-allowed"
              >
                {{ loading ? 'Enviando...' : 'Enviar mensaje' }}
              </button>
            </div>
          </form>
        </div>
      </div>
    </main>

    <Footer />
  </div>
</template>

<script setup>
import { computed, reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import Header from '../../components/layout/Header.vue'
import PublicHeader from '../../components/layout/PublicHeader.vue'
import Footer from '../../components/layout/Footer.vue'
import { contactService } from '../../services/api'

const router = useRouter()
const route = useRoute()
const isAdminContext = computed(() => route.path.startsWith('/admin'))

const loading = ref(false)
const error = ref('')

const form = reactive({
  name: '',
  email: '',
  subject: '',
  message: ''
})

async function handleSubmit() {
  loading.value = true
  error.value = ''

  try {
    await contactService.createContact({
      name: form.name,
      email: form.email,
      subject: form.subject,
      message: form.message
    })

    form.name = ''
    form.email = ''
    form.subject = ''
    form.message = ''

    router.push({ name: 'ticket-status' })
  } catch (err) {
    error.value = err?.message || 'No se pudo enviar el mensaje. Inténtalo de nuevo.'
  } finally {
    loading.value = false
  }
}
</script>
