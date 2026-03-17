<template>
  <header class="relative flex h-16 items-center justify-between whitespace-nowrap border-b border-solid border-b-[#e7ecf3] dark:border-b-slate-800 px-6 lg:px-40 bg-white dark:bg-slate-900">
    <div class="flex items-center gap-4">
      <img src="/icots.svg" alt="TechService" class="size-8 object-contain" />
      <h2 class="text-lg font-bold leading-tight tracking-[-0.015em]">TechService</h2>
    </div>
    
    <div class="flex flex-1 justify-end gap-8">
      <div class="hidden md:flex items-center gap-9">
        <router-link to="/" class="text-sm font-medium leading-normal hover:text-primary transition-colors">Inicio</router-link>
        <router-link to="/contacto" class="text-sm font-medium leading-normal hover:text-primary transition-colors">Contacto</router-link>
      </div>

      <button
        type="button"
        class="md:hidden inline-flex items-center justify-center size-10 rounded-lg border border-slate-200 dark:border-slate-700 text-slate-600 dark:text-slate-300"
        @click="isMobileMenuOpen = !isMobileMenuOpen"
        aria-label="Abrir menú"
      >
        <span class="material-symbols-outlined">{{ isMobileMenuOpen ? 'close' : 'menu' }}</span>
      </button>

      <router-link 
        to="/admin/login"
        class="hidden md:flex min-w-[84px] max-w-[480px] cursor-pointer items-center justify-center overflow-hidden rounded-lg h-10 px-4 bg-primary text-white text-sm font-bold leading-normal tracking-[0.015em]"
      >
        <span class="truncate">Login</span>
      </router-link>
    </div>

    <div
      v-if="isMobileMenuOpen"
      class="absolute left-0 right-0 top-full z-50 md:hidden border-b border-slate-200 dark:border-slate-800 bg-white dark:bg-slate-900 px-6 py-4"
    >
      <nav class="flex flex-col gap-3">
        <router-link
          to="/"
          class="text-sm font-medium text-slate-700 dark:text-slate-300 hover:text-primary"
        >
          Inicio
        </router-link>
        <router-link
          to="/contacto"
          class="text-sm font-medium text-slate-700 dark:text-slate-300 hover:text-primary"
        >
          Contacto
        </router-link>
        <router-link
          to="/admin/login"
          class="text-sm font-medium text-slate-700 dark:text-slate-300 hover:text-primary"
        >
          Login
        </router-link>
      </nav>
    </div>
  </header>
</template>

<script setup>
import { ref, watch } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const isMobileMenuOpen = ref(false)

watch(
  () => route.fullPath,
  () => {
    isMobileMenuOpen.value = false
  }
)
</script>