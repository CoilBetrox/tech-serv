import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { jwtDecode } from 'jwt-decode'

export const useAuthStore = defineStore('auth', () => {
  const token = ref(localStorage.getItem('token'))
  const user = ref(null)
  
  const isAuthenticated = computed(() => !!token.value)
  const isAdmin = computed(() => {
    // Verificar si el rol es ADMIN (con o sin prefijo ROLE_)
    const role = user.value?.role
    return role === 'ADMIN' || role === 'ROLE_ADMIN'
  })
  
  function setToken(newToken) {
    token.value = newToken
    localStorage.setItem('token', newToken)
    
    if (newToken) {
      try {
        const decoded = jwtDecode(newToken)
        console.log('Decoded token:', decoded) // Para debugging
        
        // Extraer información del token según tu backend
        user.value = {
          id: decoded.sub || decoded.userId || decoded.id,
          email: decoded.email || decoded.sub,
          role: decoded.role || decoded.authorities?.[0] || 'ADMIN'
        }
      } catch (error) {
        console.error('Error decoding token:', error)
        logout()
      }
    }
  }
  
  function logout() {
    token.value = null
    user.value = null
    localStorage.removeItem('token')
  }
  
  // Auto decode token if exists
  if (token.value) {
    try {
      const decoded = jwtDecode(token.value)
      user.value = {
        id: decoded.sub || decoded.userId || decoded.id,
        email: decoded.email || decoded.sub,
        role: decoded.role || decoded.authorities?.[0] || 'ADMIN'
      }
    } catch {
      logout()
    }
  }
  
  return { token, user, isAuthenticated, isAdmin, setToken, logout }
})