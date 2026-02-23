<template>
  <div class="relative flex justify-between w-full mt-4">
    <div class="absolute top-5 left-0 w-full h-1 bg-slate-100 dark:bg-slate-800 -z-0"></div>
    <div 
      class="absolute top-5 left-0 h-1 bg-primary -z-0 transition-all duration-500"
      :style="`width: ${progressWidth}`"
    ></div>
    
    <div 
      v-for="(step, index) in steps" 
      :key="index"
      class="relative z-10 flex flex-col items-center"
    >
      <div 
        class="size-10 rounded-full flex items-center justify-center shadow-md"
        :class="stepClasses(index)"
      >
        <span class="material-symbols-outlined text-sm">{{ step.icon }}</span>
      </div>
      <span class="mt-3 text-sm font-bold" :class="textClasses(index)">
        {{ step.label }}
      </span>
      <span v-if="step.date" class="text-[10px] text-[#4c6c9a] dark:text-slate-500">
        {{ step.date }}
      </span>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  status: {
    type: String,
    default: 'in_progress'
  },
  history: {
    type: Array,
    default: () => []
  }
})

const steps = [
  { key: 'received', label: 'Received', icon: 'check', date: '' },
  { key: 'in_progress', label: 'In Progress', icon: 'engineering', date: '' },
  { key: 'ready', label: 'Ready', icon: 'inventory_2', date: '' },
  { key: 'delivered', label: 'Delivered', icon: 'handshake', date: '' }
]

// Mapear historial a steps
steps.forEach(step => {
  const historyItem = props.history.find(h => h.status === step.key)
  if (historyItem) {
    step.date = historyItem.date
  }
})

const activeIndex = computed(() => {
  const statusOrder = ['received', 'in_progress', 'ready', 'delivered']
  return statusOrder.indexOf(props.status)
})

const progressWidth = computed(() => {
  if (activeIndex.value === -1) return '0%'
  return `${(activeIndex.value / (steps.length - 1)) * 100}%`
})

function stepClasses(index) {
  if (index <= activeIndex.value) {
    return 'bg-primary text-white ring-4 ring-primary/20'
  }
  return 'bg-slate-100 dark:bg-slate-800 text-[#4c6c9a] dark:text-slate-600'
}

function textClasses(index) {
  if (index === activeIndex.value) {
    return 'text-primary'
  } else if (index <= activeIndex.value) {
    return 'text-[#0d131b] dark:text-white'
  }
  return 'text-[#4c6c9a] dark:text-slate-500'
}
</script>