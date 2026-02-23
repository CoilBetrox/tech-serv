<template>
  <span 
    class="inline-flex items-center rounded-full px-2.5 py-0.5 text-xs font-semibold border"
    :class="statusClasses"
  >
    {{ statusText }}
  </span>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  status: {
    type: String,
    required: true,
    validator: value => ['pending', 'in_process', 'finished', 'delivered'].includes(value)
  }
})

const statusConfig = {
  pending: {
    text: 'Pending',
    classes: 'bg-amber-50 dark:bg-amber-900/20 text-amber-700 dark:text-amber-300 border-amber-100 dark:border-amber-900/50'
  },
  in_process: {
    text: 'In Process',
    classes: 'bg-blue-50 dark:bg-blue-900/20 text-blue-700 dark:text-blue-300 border-blue-100 dark:border-blue-900/50'
  },
  finished: {
    text: 'Finished',
    classes: 'bg-emerald-50 dark:bg-emerald-900/20 text-emerald-700 dark:text-emerald-300 border-emerald-100 dark:border-emerald-900/50'
  },
  delivered: {
    text: 'Delivered',
    classes: 'bg-purple-50 dark:bg-purple-900/20 text-purple-700 dark:text-purple-300 border-purple-100 dark:border-purple-900/50'
  }
}

const statusText = computed(() => statusConfig[props.status]?.text || props.status)
const statusClasses = computed(() => statusConfig[props.status]?.classes || '')
</script>