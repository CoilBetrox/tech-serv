<script setup>
import { computed } from 'vue'
import { useUIStore } from './stores/ui.store'

const uiStore = useUIStore()
const isDarkMode = computed(() => uiStore.darkMode)
</script>

<template>
  <div id="app" :class="{'dark': isDarkMode}">
    <router-view />
  </div>
</template>

<style>
@import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap');
@import url('https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:wght,FILL@100..700,0..1&display=swap');

body {
  font-family: 'Inter', sans-serif;
}
</style>
