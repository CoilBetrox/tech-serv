<template>
  <div class="relative flex h-auto min-h-screen w-full flex-col group/design-root overflow-x-hidden font-display">
    <Header />
    
    <main class="flex-1 flex flex-col items-center">
      <div class="max-w-[1200px] w-full px-4 md:px-10 py-8 flex flex-col gap-6">
        <!-- Breadcrumb -->
        <div class="flex flex-wrap items-center gap-2 text-sm">
          <router-link to="/admin/orders" class="text-slate-500 dark:text-slate-400 font-medium hover:text-primary transition-colors">
            Órdenes de servicio
          </router-link>
          <span class="text-slate-400 font-medium">/</span>
          <span class="text-slate-900 dark:text-white font-semibold">Crear nueva orden</span>
        </div>
        
        <!-- Título -->
        <div class="flex flex-col gap-2">
          <h1 class="text-slate-900 dark:text-white text-4xl font-black leading-tight tracking-tight">
            Crear nueva orden de servicio
          </h1>
          <p class="text-slate-500 dark:text-slate-400 text-base">
            Ingresa los datos del cliente y del dispositivo para iniciar la solicitud de reparación. Los campos con * son obligatorios.
          </p>
        </div>
        
        <!-- Formulario -->
        <form @submit.prevent="handleSubmit" class="grid grid-cols-1 lg:grid-cols-12 gap-8 items-start">
          <!-- Columna izquierda: Información del cliente -->
          <section class="lg:col-span-5 bg-white dark:bg-slate-900 rounded-xl border border-slate-200 dark:border-slate-800 p-6 flex flex-col gap-6 shadow-sm">
            <div class="flex items-center justify-between">
              <h3 class="text-slate-900 dark:text-white text-xl font-bold flex items-center gap-2">
                <span class="material-symbols-outlined text-primary">person</span>
                Información del cliente
              </h3>
              <span class="bg-primary/10 text-primary text-[10px] uppercase tracking-wider font-bold px-2 py-1 rounded">
                Cliente nuevo
              </span>
            </div>
            
            <!-- Email con verificación -->
            <div class="flex flex-col gap-2">
              <label class="text-slate-900 dark:text-white text-sm font-semibold">Correo del cliente *</label>
              <div class="flex w-full items-stretch rounded-lg shadow-sm">
                <input 
                  v-model="form.client.email"
                  @blur="checkExistingClient"
                  class="form-input flex-1 rounded-l-lg text-slate-900 dark:text-white focus:outline-0 focus:ring-1 focus:ring-primary border border-slate-300 dark:border-slate-700 bg-white dark:bg-slate-800 h-12 px-4 text-base placeholder:text-slate-400" 
                  placeholder="customer@example.com" 
                  type="email"
                  required
                />
                <div 
                  class="text-white flex bg-primary items-center justify-center px-4 rounded-r-lg"
                >
                  <span class="material-symbols-outlined">mail</span>
                </div>
              </div>
              <p class="text-xs text-slate-500 italic">Buscaremos automáticamente si este cliente ya existe al ingresar el correo.</p>
            </div>
            
            <!-- Campos de cliente -->
            <div class="grid grid-cols-1 gap-4">
              <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div class="flex flex-col gap-2">
                  <label class="text-slate-900 dark:text-white text-sm font-semibold">Nombre *</label>
                  <input 
                    v-model="form.client.firstName"
                    class="form-input rounded-lg border border-slate-300 dark:border-slate-700 bg-slate-50 dark:bg-slate-800 h-12 px-4 text-slate-900 dark:text-white focus:ring-primary" 
                    placeholder="Nombre" 
                    type="text"
                    required
                  />
                </div>

                <div class="flex flex-col gap-2">
                  <label class="text-slate-900 dark:text-white text-sm font-semibold">Apellido *</label>
                  <input 
                    v-model="form.client.lastName"
                    class="form-input rounded-lg border border-slate-300 dark:border-slate-700 bg-slate-50 dark:bg-slate-800 h-12 px-4 text-slate-900 dark:text-white focus:ring-primary" 
                    placeholder="Apellido" 
                    type="text"
                    required
                  />
                </div>
              </div>
              
              <div class="flex flex-col gap-2">
                <label class="text-slate-900 dark:text-white text-sm font-semibold">Teléfono</label>
                <input 
                  v-model="form.client.phone"
                  class="form-input rounded-lg border border-slate-300 dark:border-slate-700 bg-slate-50 dark:bg-slate-800 h-12 px-4 text-slate-900 dark:text-white focus:ring-primary" 
                  placeholder="+1 (555) 000-0000" 
                  type="tel"
                  required
                />
              </div>
              
              <div class="flex flex-col gap-2">
                <label class="text-slate-900 dark:text-white text-sm font-semibold">Contacto alternativo (opcional)</label>
                <input 
                  v-model="form.client.alternativePhone"
                  class="form-input rounded-lg border border-slate-300 dark:border-slate-700 bg-white dark:bg-slate-800 h-12 px-4 text-slate-900 dark:text-white focus:ring-primary" 
                  placeholder="Teléfono secundario o persona de contacto" 
                  type="text"
                />
              </div>
            </div>
          </section>
          
          <!-- Columna derecha: Detalles del dispositivo -->
          <section class="lg:col-span-7 flex flex-col gap-6">
            <div class="bg-white dark:bg-slate-900 rounded-xl border border-slate-200 dark:border-slate-800 p-6 flex flex-col gap-6 shadow-sm">
              <h3 class="text-slate-900 dark:text-white text-xl font-bold flex items-center gap-2">
                <span class="material-symbols-outlined text-primary">devices</span>
                Detalles del dispositivo
              </h3>
              
              <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div class="flex flex-col gap-2">
                  <label class="text-slate-900 dark:text-white text-sm font-semibold">Marca *</label>
                  <input 
                    v-model="form.device.brand"
                    class="form-input rounded-lg border border-slate-300 dark:border-slate-700 bg-white dark:bg-slate-800 h-12 px-4 text-slate-900 dark:text-white focus:ring-primary" 
                    placeholder="e.g., Apple, Dell, Lenovo" 
                    type="text"
                    required
                  />
                </div>
                
                <div class="flex flex-col gap-2">
                  <label class="text-slate-900 dark:text-white text-sm font-semibold">Modelo *</label>
                  <input 
                    v-model="form.device.model"
                    class="form-input rounded-lg border border-slate-300 dark:border-slate-700 bg-white dark:bg-slate-800 h-12 px-4 text-slate-900 dark:text-white focus:ring-primary" 
                    placeholder="e.g., MacBook Pro M3" 
                    type="text"
                    required
                  />
                </div>
                
                <div class="flex flex-col gap-2">
                  <label class="text-slate-900 dark:text-white text-sm font-semibold">Número de serie *</label>
                  <div class="relative">
                    <input 
                      v-model="form.device.serialNumber"
                      class="form-input w-full rounded-lg border border-slate-300 dark:border-slate-700 bg-white dark:bg-slate-800 h-12 px-4 text-slate-900 dark:text-white focus:ring-primary" 
                      placeholder="S/N: 123456789" 
                      type="text"
                      required
                    />
                    <span 
                      class="absolute right-3 top-3 text-slate-400 material-symbols-outlined cursor-help" 
                      title="La ubicación del número de serie varía según el fabricante"
                    >
                      info
                    </span>
                  </div>
                </div>

                <div class="flex flex-col gap-2">
                  <label class="text-slate-900 dark:text-white text-sm font-semibold">Costo estimado *</label>
                  <div class="relative">
                    <span class="absolute left-3 top-3 text-slate-400 font-semibold">$</span>
                    <input 
                      v-model.number="form.device.estimatedCost"
                      class="form-input w-full rounded-lg border border-slate-300 dark:border-slate-700 bg-white dark:bg-slate-800 h-12 pl-8 pr-4 text-slate-900 dark:text-white focus:ring-primary" 
                      placeholder="0.00" 
                      type="number"
                      step="0.01"
                      min="0"
                      required
                    />
                  </div>
                </div>
              </div>
              
              <div class="flex flex-col gap-2">
                <label class="text-slate-900 dark:text-white text-sm font-semibold">Descripción del problema *</label>
                <textarea 
                  v-model="form.device.problem"
                  class="form-textarea rounded-lg border border-slate-300 dark:border-slate-700 bg-white dark:bg-slate-800 min-h-[160px] p-4 text-slate-900 dark:text-white focus:ring-primary placeholder:text-slate-400" 
                  placeholder="Describe el problema en detalle. ¿Qué ocurrió? ¿Hay códigos de error?"
                  required
                ></textarea>
              </div>
            </div>
            
            <!-- Botones de acción -->
            <div class="flex items-center justify-end gap-4 pb-12">
              <router-link 
                to="/admin/orders"
                class="px-6 h-12 rounded-lg border border-slate-300 dark:border-slate-700 text-slate-700 dark:text-slate-300 font-bold hover:bg-slate-50 dark:hover:bg-slate-800 transition-colors inline-flex items-center"
              >
                Cancelar
              </router-link>
              
              <button 
                type="submit"
                class="px-8 h-12 rounded-lg bg-primary text-white font-bold shadow-lg shadow-primary/20 hover:bg-primary/90 transition-all flex items-center gap-2"
                :disabled="loading"
              >
                <span class="material-symbols-outlined">{{ loading ? 'hourglass_empty' : 'assignment_add' }}</span>
                {{ loading ? 'Creando...' : 'Crear orden' }}
              </button>
            </div>
          </section>
        </form>
      </div>
    </main>
    
    <Footer />
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useOrderStore } from '../../stores/order.store'
import { orderService } from '../../services/api'
import Header from '../../components/layout/Header.vue'
import Footer from '../../components/layout/Footer.vue'

const router = useRouter()
const orderStore = useOrderStore()
const loading = ref(false)

const form = reactive({
  client: {
    email: '',
    firstName: '',
    lastName: '',
    phone: '',
    alternativePhone: ''
  },
  device: {
    brand: '',
    model: '',
    serialNumber: '',
    estimatedCost: 0,
    problem: ''
  }
})

async function checkExistingClient() {
  if (!form.client.email) return
  
  loading.value = true
  try {
    const token = localStorage.getItem('token')
    const existing = await orderService.findCustomerByEmail(token, form.client.email)
    if (existing) {
      form.client.firstName = existing.firstName || form.client.firstName
      form.client.lastName = existing.lastName || form.client.lastName
      form.client.phone = existing.phone || form.client.phone
    }
  } catch (error) {
    console.error('Error al buscar cliente:', error)
  } finally {
    loading.value = false
  }
}

async function handleSubmit() {
  loading.value = true
  
  try {
    const token = localStorage.getItem('token')
    const existingCustomer = await orderService.findCustomerByEmail(token, form.client.email)

    const orderPayload = {
      description: form.device.problem,
      estimatedCost: form.device.estimatedCost,
      device: {
        type: 'Device',
        brand: form.device.brand,
        model: form.device.model,
        serialNumber: form.device.serialNumber,
        customer: existingCustomer
          ? { id: existingCustomer.id }
          : {
              firstName: form.client.firstName,
              lastName: form.client.lastName,
              email: form.client.email,
              phone: form.client.phone
            }
      }
    }

    await orderStore.createOrder(orderPayload)
    
    // Redirigir a la lista de órdenes
    router.push('/admin/orders')
    
  } catch (error) {
    console.error('Error al crear orden:', error)
  } finally {
    loading.value = false
  }
}
</script>