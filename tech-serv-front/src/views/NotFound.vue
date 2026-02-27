<template>
  <div class="min-h-screen bg-background-light dark:bg-background-dark flex items-center justify-center p-4">
    <div class="fixed inset-0 z-0 overflow-hidden opacity-30">
      <div class="absolute inset-0 bg-primary/5 blur-[100px]"></div>
    </div>
    
    <div class="relative z-10 w-full max-w-[600px] text-center">
      <div class="bg-white dark:bg-slate-900 rounded-2xl shadow-2xl p-12 border border-slate-200 dark:border-slate-800">
        <!-- Icono grande -->
        <div class="flex justify-center mb-8">
          <div class="relative">
            <div class="absolute inset-0 bg-primary/20 rounded-full blur-xl"></div>
            <div class="relative bg-primary/10 text-primary w-32 h-32 rounded-full flex items-center justify-center">
              <span class="material-symbols-outlined text-7xl">error</span>
            </div>
          </div>
        </div>
        
        <!-- Código 404 -->
        <h1 class="text-8xl font-black text-primary mb-4">404</h1>
        
        <!-- Mensaje -->
        <h2 class="text-2xl font-bold text-slate-900 dark:text-white mb-3">
          Página no encontrada
        </h2>
        
        <p class="text-slate-500 dark:text-slate-400 text-lg mb-8 max-w-md mx-auto">
          La página que buscas no existe o fue movida. Te ayudamos a volver al flujo principal.
        </p>
        
        <!-- Opciones de navegación -->
        <div class="flex flex-col sm:flex-row gap-4 justify-center">
          <router-link 
            to="/"
            class="inline-flex items-center justify-center gap-2 px-6 h-12 bg-primary text-white rounded-lg font-bold hover:bg-primary/90 transition-all shadow-lg shadow-primary/20"
          >
            <span class="material-symbols-outlined">home</span>
            Ir al inicio
          </router-link>
          
          <router-link 
            to="/admin/orders"
            class="inline-flex items-center justify-center gap-2 px-6 h-12 bg-slate-100 dark:bg-slate-800 text-slate-700 dark:text-slate-300 rounded-lg font-bold hover:bg-slate-200 dark:hover:bg-slate-700 transition-all"
          >
            <span class="material-symbols-outlined">assignment</span>
            Panel de administración
          </router-link>
        </div>
        
        <!-- Acciones rápidas -->
        <div class="mt-12 pt-8 border-t border-slate-100 dark:border-slate-800">
          <p class="text-sm text-slate-400 mb-4">Acciones rápidas</p>
          <div class="flex flex-wrap gap-3 justify-center">
            <router-link 
              to="/"
              class="text-xs text-slate-500 hover:text-primary transition-colors flex items-center gap-1"
            >
              <span class="material-symbols-outlined text-sm">search</span>
              Consultar estado del ticket
            </router-link>
            <span class="text-slate-300">•</span>
            <router-link 
              to="/admin/login"
              class="text-xs text-slate-500 hover:text-primary transition-colors flex items-center gap-1"
            >
              <span class="material-symbols-outlined text-sm">login</span>
              Login
            </router-link>
            <span class="text-slate-300">•</span>
            <a 
              href="#"
              class="text-xs text-slate-500 hover:text-primary transition-colors flex items-center gap-1"
            >
              <span class="material-symbols-outlined text-sm">support</span>
              Centro de ayuda
            </a>
          </div>
        </div>
      </div>
      
      <!-- Footer -->
      <p class="text-slate-400 text-xs mt-8">
        © 2024 TechFix Manager. Todos los derechos reservados.
      </p>
    </div>
  </div>
</template>

<script setup>
// No necesita lógica por ahora
</script>

<style scoped>
/* Estilos específicos si son necesarios */
</style>