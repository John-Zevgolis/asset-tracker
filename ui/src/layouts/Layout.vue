<template>
  <div class="min-h-screen flex flex-col bg-gray-50">
    <Header />
    <slot />
    <Footer />
  </div>
</template>

<script setup lang="ts">
import Footer from '@/components/Footer.vue';
import Header from '@/components/Header.vue';
</script>

<style scoped></style>
