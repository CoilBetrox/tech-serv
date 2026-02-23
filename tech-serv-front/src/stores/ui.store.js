import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useUIStore = defineStore('ui', () => {
  const darkMode = ref(localStorage.getItem('darkMode') === 'true' || false)
  const activeModal = ref(null)
  const modalData = ref(null)
  
  function toggleDarkMode() {
    darkMode.value = !darkMode.value
    localStorage.setItem('darkMode', darkMode.value)
  }
  
  function openModal(modalName, data = null) {
    activeModal.value = modalName
    modalData.value = data
  }
  
  function closeModal() {
    activeModal.value = null
    modalData.value = null
  }
  
  return { darkMode, activeModal, modalData, toggleDarkMode, openModal, closeModal }
})