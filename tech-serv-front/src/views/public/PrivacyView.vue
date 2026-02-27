<template>
  <div class="relative flex min-h-screen w-full flex-col overflow-x-hidden">
    <PublicHeader />

    <main class="flex flex-1 flex-col items-center py-12 px-4 sm:px-10 lg:px-40">
      <div class="max-w-[900px] w-full">
        <div class="mb-10">
          <h1 class="text-[#0d131b] dark:text-white text-3xl sm:text-4xl font-bold leading-tight">
            Política de Privacidad
          </h1>
          <p class="text-[#4c6c9a] dark:text-slate-400 mt-3 text-base">
            Actualizado el 26 de febrero del 2026.
          </p>
        </div>

        <div class="space-y-6">
          <section class="bg-white dark:bg-slate-900 rounded-xl border border-[#e7ecf3] dark:border-slate-800 p-6">
            <h2 class="text-xl font-bold text-slate-900 dark:text-white">Introducción</h2>
            <p class="mt-3 text-sm text-slate-500 dark:text-slate-400">
              En TechServ, valoramos su privacidad y nos comprometemos a proteger sus datos personales.
            </p>
            <p class="mt-3 text-sm text-slate-500 dark:text-slate-400">
              Esta Política de Privacidad explica cómo recopilamos, usamos y protegemos la información dentro de nuestra plataforma de gestión de servicios técnicos.
            </p>
          </section>

          <section class="bg-white dark:bg-slate-900 rounded-xl border border-[#e7ecf3] dark:border-slate-800 p-6">
            <h2 class="text-xl font-bold text-slate-900 dark:text-white">Información que Recopilamos</h2>
            <p class="mt-3 text-sm text-slate-500 dark:text-slate-400">
              Para el funcionamiento óptimo de la plataforma, recopilamos los siguientes datos:
            </p> 
            <ul class="mt-3 list-disc list-inside text-sm text-slate-500 dark:text-slate-400">
                <li>Datos del Administrador/Técnico: Nombre, correo electrónico y credenciales de acceso.</li>
                <li>Datos del Cliente Final: Nombre, número de teléfono y, opcionalmente, correo electrónico.</li>
                <li>Datos del Servicio: Descripción del dispositivo, fallas reportadas, estados de reparación y presupuestos.</li>
            </ul>
          </section>

          <section class="bg-white dark:bg-slate-900 rounded-xl border border-[#e7ecf3] dark:border-slate-800 p-6">
            <h2 class="text-xl font-bold text-slate-900 dark:text-white">Uso de la Información</h2>
            <p class="mt-3 text-sm text-slate-500 dark:text-slate-400">
              La información recopilada se utiliza exclusivamente para:
            </p> 
            <ul class="mt-3 list-disc list-inside text-sm text-slate-500 dark:text-slate-400">
                <li>Generar y gestionar tickets de servicio técnico.</li>
                <li>Permitir que el cliente final consulte el estado de su reparación en tiempo real.</li>
                <li>Facilitar la comunicación entre el taller y el dueño del dispositivo.</li>
                <li>Mejorar la funcionalidad técnica de la plataforma.</li>
            </ul>
          </section>

          <section class="bg-white dark:bg-slate-900 rounded-xl border border-[#e7ecf3] dark:border-slate-800 p-6">
            <h2 class="text-xl font-bold text-slate-900 dark:text-white">Consentimiento</h2>
            <p class="mt-3 text-sm text-slate-500 dark:text-slate-400">
              Al utilizar TechServ o registrar los datos de sus clientes en la plataforma, usted declara que tiene el consentimiento de dichos terceros para el tratamiento de su información con fines estrictamente técnicos y de notificación.
            </p>
          </section>

          <section class="bg-white dark:bg-slate-900 rounded-xl border border-[#e7ecf3] dark:border-slate-800 p-6">
            <h2 class="text-xl font-bold text-slate-900 dark:text-white">Protección de Datos y Seguridad</h2>
            <p class="mt-3 text-sm text-slate-500 dark:text-slate-400">
              Implementamos medidas de seguridad técnicas para proteger su información:
            </p> 
            <ul class="mt-3 list-disc list-inside text-sm text-slate-500 dark:text-slate-400">
                <li>Autenticación Segura: Uso de JSON Web Tokens (JWT) para asegurar que solo personal autorizado acceda al panel de administración.</li>
                <li>Aislamiento de Datos: La base de datos opera en entornos de contenedores (Docker) aislados para prevenir accesos no autorizados.</li>
                <li>Cifrado: Aplicamos protocolos de transferencia segura para proteger los datos en tránsito.</li>
            </ul>
          </section>

          <section class="bg-white dark:bg-slate-900 rounded-xl border border-[#e7ecf3] dark:border-slate-800 p-6">
            <h2 class="text-xl font-bold text-slate-900 dark:text-white">Terceros y Divulgación</h2>
            <p class="mt-3 text-sm text-slate-500 dark:text-slate-400">
              TechServ no vende, alquila ni comercializa datos personales a terceros. La información solo es accesible por el administrador del taller contratante. Los datos pueden ser alojados en proveedores de infraestructura en la nube (como Oracle Cloud) bajo estrictos estándares de seguridad, pero estos proveedores no tienen acceso a su información privada.
            </p>
          </section>

          <section class="bg-white dark:bg-slate-900 rounded-xl border border-[#e7ecf3] dark:border-slate-800 p-6">
            <h2 class="text-xl font-bold text-slate-900 dark:text-white">Derechos de los Usuarios (ARCO)</h2>
            <p class="mt-3 text-sm text-slate-500 dark:text-slate-400">
              Los usuarios (técnicos y clientes) tienen derecho a:
            </p> 
            <ul class="mt-3 list-disc list-inside text-sm text-slate-500 dark:text-slate-400">
                <li>Acceder a sus datos personales almacenados.</li>
                <li>Rectificar información incorrecta o desactualizada.</li>
                <li>Cancelar o solicitar la eliminación de sus datos del sistema.</li>
                <li>Oponerse al tratamiento de sus datos para fines distintos a la gestión del ticket.</li>
            </ul>
          </section>

          <section class="bg-white dark:bg-slate-900 rounded-xl border border-[#e7ecf3] dark:border-slate-800 p-6">
            <h2 class="text-xl font-bold text-slate-900 dark:text-white">Almacenamiento y Retención</h2>
            <p class="mt-3 text-sm text-slate-500 dark:text-slate-400">
              Conservamos los datos de los tickets solo durante el tiempo necesario para cumplir con la gestión del servicio técnico y para fines de historial administrativo del taller, a menos que el usuario solicite su eliminación definitiva.
            </p>
          </section>

          <section class="bg-white dark:bg-slate-900 rounded-xl border border-[#e7ecf3] dark:border-slate-800 p-6">
            <h2 class="text-xl font-bold text-slate-900 dark:text-white">Cookies y Tecnologías de Seguimiento</h2>
            <p class="mt-3 text-sm text-slate-500 dark:text-slate-400">
              Utilizamos cookies esenciales para mantener la sesión del administrador activa y garantizar la seguridad del sitio. No utilizamos cookies de seguimiento publicitario ni de análisis de comportamiento de terceros.
            </p>
          </section>

          <section class="bg-white dark:bg-slate-900 rounded-xl border border-[#e7ecf3] dark:border-slate-800 p-6">
            <h2 class="text-xl font-bold text-slate-900 dark:text-white">Cambios en la Política de Privacidad</h2>
            <p class="mt-3 text-sm text-slate-500 dark:text-slate-400">
              Nos reservamos el derecho de actualizar esta política para reflejar cambios en nuestras prácticas o por requisitos legales. Notificaremos cualquier cambio significativo a través de la plataforma.
            </p>
          </section>

          <section class="bg-white dark:bg-slate-900 rounded-xl border border-[#e7ecf3] dark:border-slate-800 p-6">
            <h2 class="text-xl font-bold text-slate-900 dark:text-white">Contacto</h2>
            <p class="mt-3 text-sm text-slate-500 dark:text-slate-400">
              Si tiene preguntas sobre esta Política de Privacidad o el manejo de sus datos, puede contactarnos en:
            </p> 
            <ul class="mt-3 list-disc list-inside text-sm text-slate-500 dark:text-slate-400">
                <li>Responsable: Roberth Daniel T.</li>
                <li>Correo electrónico: roberthdanielt@gmail.com</li>
                <li>Teléfono: +593 99 801 0276</li>
            </ul>
          </section>
        </div>
      </div>
    </main>

    <Footer />
  </div>
</template>

<script setup>
import PublicHeader from '../../components/layout/PublicHeader.vue'
import Footer from '../../components/layout/Footer.vue'
</script>
