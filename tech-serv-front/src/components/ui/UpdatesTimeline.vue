<template>
  <div v-if="updates && updates.length" :class="wrapperClass">
    <div class="flex items-center gap-2 mb-4">
      <span class="material-symbols-outlined text-primary">history</span>
      <h4 class="text-sm font-bold text-slate-900 dark:text-white uppercase tracking-wider">{{ title }}</h4>
    </div>

    <div class="space-y-3">
      <div
        v-for="(update, index) in updates"
        :key="index"
        class="p-4 rounded-lg border border-slate-200 dark:border-slate-700 bg-slate-50 dark:bg-slate-800/50"
      >
        <div class="flex items-start gap-3">
          <div class="mt-0.5">
            <div class="size-2 rounded-full bg-primary"></div>
          </div>
          <div class="flex-1 min-w-0">
            <div class="flex items-center gap-2 mb-1">
              <span class="text-xs font-bold text-primary uppercase">{{ update.stage }}</span>
              <span v-if="update.date" class="text-xs text-slate-400">• {{ update.date }}</span>
            </div>
            <p class="text-sm text-slate-700 dark:text-slate-300 whitespace-pre-wrap break-words [overflow-wrap:anywhere] leading-relaxed">
              {{ update.message }}
            </p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
defineProps({
  updates: {
    type: Array,
    default: () => []
  },
  title: {
    type: String,
    default: 'Actualizaciones'
  },
  wrapperClass: {
    type: String,
    default: ''
  }
})
</script>
