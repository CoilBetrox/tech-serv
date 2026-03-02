// Configuración base de la API
const API_BASE_URL = window.location.hostname === 'localhost' 
  ? 'http://localhost:8080/api/v1' 
  : '/api/v1';

function getClientTimeZone() {
  return Intl.DateTimeFormat().resolvedOptions().timeZone || 'UTC'
}

async function parseResponseBody(response) {
  const contentType = response.headers.get('content-type') || ''
  if (!contentType.includes('application/json')) {
    return null
  }

  try {
    return await response.json()
  } catch {
    return null
  }
}

// Helper para manejar respuestas
async function handleResponse(response) {
  const data = await parseResponseBody(response)
  
  if (!response.ok) {
    // Si es 401, podría ser token expirado
    if (response.status === 401) {
      const isLoginRequest = response.url?.includes('/auth/login')
      if (!isLoginRequest) {
        localStorage.removeItem('token')
        if (window.location.pathname !== '/') {
          window.location.assign('/')
        }
      }
    }
    
    const error = (data && data.message) || response.statusText
    return Promise.reject(new Error(error))
  }
  
  return data
}

// Servicio de autenticación
export const authService = {
  async login(email, password) {
    const response = await fetch(`${API_BASE_URL}/auth/login`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ email, password })
    })
    
    return handleResponse(response)
  },
  
  async register(email, password, role = 'CUSTOMER') {
    const response = await fetch(`${API_BASE_URL}/auth/register`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ email, password, role })
    })
    
    return handleResponse(response)
  }
}

// Servicio para órdenes (con token)
export const orderService = {
  async getAllOrders(token) {
    const response = await fetch(`${API_BASE_URL}/services/created-by-me`, {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    })
    return handleResponse(response)
  },
  
  async createOrder(token, orderData) {
    const response = await fetch(`${API_BASE_URL}/services`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`,
        'X-Timezone': getClientTimeZone()
      },
      body: JSON.stringify(orderData)
    })
    return handleResponse(response)
  },

  async findCustomerByEmail(token, email) {
    const response = await fetch(`${API_BASE_URL}/customers/by-email?email=${encodeURIComponent(email)}`, {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    })

    if (response.status === 404) {
      return null
    }

    return handleResponse(response)
  },

  async getMyCustomers(token) {
    const response = await fetch(`${API_BASE_URL}/customers/my-customers`, {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    })
    return handleResponse(response)
  },
  
  async updateOrderStatus(token, id, status, message = null) {
    const payload = { status }
    if (message) {
      payload.message = message
    }
    
    const response = await fetch(`${API_BASE_URL}/services/${id}/status`, {
      method: 'PATCH',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`,
        'X-Timezone': getClientTimeZone()
      },
      body: JSON.stringify(payload)
    })
    return handleResponse(response)
  },
  
  async getOrderById(token, id) {
    const response = await fetch(`${API_BASE_URL}/services/${id}`, {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    })
    return handleResponse(response)
  }
}

// Servicio público para tickets (sin token)
export const publicService = {
  async getTicketStatus(ticketNumber) {
    const response = await fetch(`${API_BASE_URL}/services/ticket/${encodeURIComponent(ticketNumber)}`)
    return handleResponse(response)
  }
}

export const contactService = {
  async createContact(contactData) {
    const response = await fetch(`${API_BASE_URL}/contacts`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(contactData)
    })
    return handleResponse(response)
  }
}