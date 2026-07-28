<template>
  <header class="bg-white border-b border-gray-200 sticky top-0 z-40">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <div class="flex justify-between h-16 items-center">
        <div class="flex items-center space-x-8">
          <RouterLink
            to="/"
            class="text-xl font-bold text-gray-900 flex items-center gap-2"
          >
            <span>📦</span> Asset Tracker
          </RouterLink>
          <nav class="hidden md:flex space-x-4">
            <RouterLink
              to="/"
              exact-active-class="text-blue-600 bg-blue-50 font-semibold"
              class="px-3 py-2 rounded-md text-sm font-medium text-gray-600 hover:text-blue-600 hover:bg-gray-50 transition-colors"
            >
              Dashboard
            </RouterLink>
            <RouterLink
              to="/assets"
              active-class="text-blue-600 bg-blue-50 font-semibold"
              class="px-3 py-2 rounded-md text-sm font-medium text-gray-600 hover:text-blue-600 hover:bg-gray-50 transition-colors"
            >
              Assets
            </RouterLink>
            <RouterLink
              to="/employees"
              active-class="text-blue-600 bg-blue-50 font-semibold"
              class="px-3 py-2 rounded-md text-sm font-medium text-gray-600 hover:text-blue-600 hover:bg-gray-50 transition-colors"
            >
              Employees
            </RouterLink>
          </nav>
        </div>
        <div class="hidden md:flex items-center">
          <button
            @click="handleLogout"
            class="px-3 py-1.5 border border-gray-300 text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-red-50 hover:text-red-600 hover:border-red-200 transition-colors cursor-pointer"
          >
            Logout
          </button>
        </div>
        <div class="flex items-center md:hidden">
          <button
            @click="isMobileMenuOpen = !isMobileMenuOpen"
            type="button"
            class="p-2 rounded-md text-gray-600 hover:text-gray-900 hover:bg-gray-100 focus:outline-none cursor-pointer"
            aria-label="Toggle menu"
          >
            <svg
              v-if="!isMobileMenuOpen"
              class="w-6 h-6"
              fill="none"
              stroke="currentColor"
              viewBox="0 0 24 24"
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                stroke-width="2"
                d="M4 6h16M4 12h16M4 18h16"
              />
            </svg>
            <svg
              v-else
              class="w-6 h-6"
              fill="none"
              stroke="currentColor"
              viewBox="0 0 24 24"
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                stroke-width="2"
                d="M6 18L18 6M6 6l12 12"
              />
            </svg>
          </button>
        </div>
      </div>
    </div>
    <div
      v-if="isMobileMenuOpen"
      class="md:hidden absolute top-full left-0 w-full border-b border-gray-200 bg-white px-4 pt-2 pb-4 space-y-1 shadow-lg z-50"
    >
      <RouterLink
        to="/"
        @click="isMobileMenuOpen = false"
        exact-active-class="text-blue-600 bg-blue-50 font-semibold"
        class="block px-3 py-2 rounded-md text-base font-medium text-gray-600 hover:text-blue-600 hover:bg-gray-50 transition-colors"
      >
        Dashboard
      </RouterLink>
      <RouterLink
        to="/assets"
        @click="isMobileMenuOpen = false"
        active-class="text-blue-600 bg-blue-50 font-semibold"
        class="block px-3 py-2 rounded-md text-base font-medium text-gray-600 hover:text-blue-600 hover:bg-gray-50 transition-colors"
      >
        Assets
      </RouterLink>
      <RouterLink
        to="/employees"
        @click="isMobileMenuOpen = false"
        active-class="text-blue-600 bg-blue-50 font-semibold"
        class="block px-3 py-2 rounded-md text-base font-medium text-gray-600 hover:text-blue-600 hover:bg-gray-50 transition-colors"
      >
        Employees
      </RouterLink>
      <div class="pt-2 border-t border-gray-100">
        <button
          @click="handleLogout"
          class="w-full text-left px-3 py-2 text-base font-medium text-red-600 hover:bg-red-50 rounded-md transition-colors cursor-pointer"
        >
          Logout
        </button>
      </div>
    </div>
  </header>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { RouterLink, useRouter } from 'vue-router';
import { useAuthStore } from '@/stores/auth';

const authStore = useAuthStore();
const router = useRouter();

const isMobileMenuOpen = ref(false);

const handleLogout = () => {
  isMobileMenuOpen.value = false;
  authStore.logout();
  router.push({ name: 'Login' });
};
</script>

<style scoped></style>
