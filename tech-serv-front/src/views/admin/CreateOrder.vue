<template>
  <div class="relative flex h-auto min-h-screen w-full flex-col group/design-root overflow-x-hidden font-display">
    <Header />
    
    <main class="flex-1 flex flex-col items-center">
      <div class="max-w-[1200px] w-full px-4 md:px-10 py-8 flex flex-col gap-6">
        <!-- Breadcrumb -->
        <div class="flex flex-wrap items-center gap-2 text-sm">
          <router-link to="/admin/orders" class="text-slate-500 dark:text-slate-400 font-medium hover:text-primary transition-colors">
            Service Orders
          </router-link>
          <span class="text-slate-400 font-medium">/</span>
          <span class="text-slate-900 dark:text-white font-semibold">Create New Order</span>
        </div>
        
        <!-- Título -->
        <div class="flex flex-col gap-2">
          <h1 class="text-slate-900 dark:text-white text-4xl font-black leading-tight tracking-tight">
            Create New Service Order
          </h1>
          <p class="text-slate-500 dark:text-slate-400 text-base">
            Enter client and device details to initiate a repair request. All fields marked with * are required.
          </p>
        </div>
        
        <!-- Formulario -->
        <form @submit.prevent="handleSubmit" class="grid grid-cols-1 lg:grid-cols-12 gap-8 items-start">
          <!-- Columna izquierda: Información del cliente -->
          <section class="lg:col-span-5 bg-white dark:bg-slate-900 rounded-xl border border-slate-200 dark:border-slate-800 p-6 flex flex-col gap-6 shadow-sm">
            <div class="flex items-center justify-between">
              <h3 class="text-slate-900 dark:text-white text-xl font-bold flex items-center gap-2">
                <span class="material-symbols-outlined text-primary">person</span>
                Client Information
              </h3>
              <span class="bg-primary/10 text-primary text-[10px] uppercase tracking-wider font-bold px-2 py-1 rounded">
                New Client
              </span>
            </div>
            
            <!-- Email con verificación -->
            <div class="flex flex-col gap-2">
              <label class="text-slate-900 dark:text-white text-sm font-semibold">Client Email *</label>
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
                  class="text-white flex bg-primary items-center justify-center px-4 rounded-r-lg cursor-pointer hover:bg-primary/90"
                  @click="checkExistingClient"
                >
                  <span class="material-symbols-outlined">check_circle</span>
                </div>
              </div>
              <p class="text-xs text-slate-500 italic">Enter email to auto-fill existing records.</p>
            </div>
            
            <!-- Campos de cliente -->
            <div class="grid grid-cols-1 gap-4">
              <div class="flex flex-col gap-2">
                <label class="text-slate-900 dark:text-white text-sm font-semibold">Full Name</label>
                <input 
                  v-model="form.client.name"
                  class="form-input rounded-lg border border-slate-300 dark:border-slate-700 bg-slate-50 dark:bg-slate-800 h-12 px-4 text-slate-900 dark:text-white focus:ring-primary" 
                  placeholder="Full name" 
                  type="text"
                  required
                />
              </div>
              
              <div class="flex flex-col gap-2">
                <label class="text-slate-900 dark:text-white text-sm font-semibold">Phone Number</label>
                <input 
                  v-model="form.client.phone"
                  class="form-input rounded-lg border border-slate-300 dark:border-slate-700 bg-slate-50 dark:bg-slate-800 h-12 px-4 text-slate-900 dark:text-white focus:ring-primary" 
                  placeholder="+1 (555) 000-0000" 
                  type="tel"
                  required
                />
              </div>
              
              <div class="flex flex-col gap-2">
                <label class="text-slate-900 dark:text-white text-sm font-semibold">Alternative Contact (Optional)</label>
                <input 
                  v-model="form.client.alternativePhone"
                  class="form-input rounded-lg border border-slate-300 dark:border-slate-700 bg-white dark:bg-slate-800 h-12 px-4 text-slate-900 dark:text-white focus:ring-primary" 
                  placeholder="Secondary phone or contact person" 
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
                Device Details
              </h3>
              
              <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div class="flex flex-col gap-2">
                  <label class="text-slate-900 dark:text-white text-sm font-semibold">Brand *</label>
                  <input 
                    v-model="form.device.brand"
                    class="form-input rounded-lg border border-slate-300 dark:border-slate-700 bg-white dark:bg-slate-800 h-12 px-4 text-slate-900 dark:text-white focus:ring-primary" 
                    placeholder="e.g., Apple, Dell, Lenovo" 
                    type="text"
                    required
                  />
                </div>
                
                <div class="flex flex-col gap-2">
                  <label class="text-slate-900 dark:text-white text-sm font-semibold">Model *</label>
                  <input 
                    v-model="form.device.model"
                    class="form-input rounded-lg border border-slate-300 dark:border-slate-700 bg-white dark:bg-slate-800 h-12 px-4 text-slate-900 dark:text-white focus:ring-primary" 
                    placeholder="e.g., MacBook Pro M3" 
                    type="text"
                    required
                  />
                </div>
                
                <div class="md:col-span-2 flex flex-col gap-2">
                  <label class="text-slate-900 dark:text-white text-sm font-semibold">Serial Number *</label>
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
                      title="Location of serial number varies by manufacturer"
                    >
                      info
                    </span>
                  </div>
                </div>
              </div>
              
              <div class="flex flex-col gap-2">
                <label class="text-slate-900 dark:text-white text-sm font-semibold">Problem Description *</label>
                <textarea 
                  v-model="form.device.problem"
                  class="form-textarea rounded-lg border border-slate-300 dark:border-slate-700 bg-white dark:bg-slate-800 min-h-[160px] p-4 text-slate-900 dark:text-white focus:ring-primary placeholder:text-slate-400" 
                  placeholder="Please describe the issue in detail. What happened? Are there any error codes?"
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
                Cancel
              </router-link>
              
              <button 
                type="submit"
                class="px-8 h-12 rounded-lg bg-primary text-white font-bold shadow-lg shadow-primary/20 hover:bg-primary/90 transition-all flex items-center gap-2"
                :disabled="loading"
              >
                <span class="material-symbols-outlined">{{ loading ? 'hourglass_empty' : 'assignment_add' }}</span>
                {{ loading ? 'Creating...' : 'Create Order' }}
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
import Header from '../../components/layout/Header.vue'
import Footer from '../../components/layout/Footer.vue'

const router = useRouter()
const orderStore = useOrderStore()
const loading = ref(false)

const form = reactive({
  client: {
    email: '',
    name: '',
    phone: '',
    alternativePhone: ''
  },
  device: {
    brand: '',
    model: '',
    serialNumber: '',
    problem: ''
  }
})

async function checkExistingClient() {
  if (!form.client.email) return
  
  loading.value = true
  try {
    // Aquí iría la llamada a la API para verificar si el cliente existe
    // Simulación:
    setTimeout(() => {
      if (form.client.email === 'john.doe@email.com') {
        form.client.name = 'John Doe'
        form.client.phone = '+1 (202) 555-0156'
      }
      loading.value = false
    }, 500)
  } catch (error) {
    console.error('Error checking client:', error)
    loading.value = false
  }
}

async function handleSubmit() {
  loading.value = true
  
  try {
    // Crear el objeto de orden en el formato que espera el store
    const newOrder = {
      clientName: form.client.name,
      email: form.client.email,
      phone: form.client.phone,
      alternativePhone: form.client.alternativePhone,
      device: `${form.device.brand} ${form.device.model}`,
      brand: form.device.brand,
      model: form.device.model,
      serialNumber: form.device.serialNumber,
      problemDescription: form.device.problem,
      status: 'pending',
      date: new Date().toLocaleDateString('en-US', { month: 'short', day: 'numeric', year: 'numeric' })
    }
    
    // Guardar en el store
    const orderId = orderStore.createOrder(newOrder)
    
    // Redirigir a la lista de órdenes
    router.push('/admin/orders')
    
  } catch (error) {
    console.error('Error creating order:', error)
  } finally {
    loading.value = false
  }
}
</script>