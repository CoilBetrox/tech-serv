<template>
  <footer class="py-6 px-10 border-t border-slate-200 dark:border-slate-800">
    <div class="max-w-[1200px] mx-auto flex flex-col md:flex-row justify-between items-center gap-4">
      <p class="text-xs text-slate-400">© 2026 TechService. Todos los derechos reservados.</p>
      <div class="flex gap-6">
        <router-link class="text-xs text-slate-400 hover:text-primary" to="/terminos">Términos</router-link>
        <router-link class="text-xs text-slate-400 hover:text-primary" to="/privacidad">Privacidad</router-link>
      </div>
    </div>
  </footer>
</template>