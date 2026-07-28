<template>
  <div
    class="min-h-screen flex items-center justify-center bg-gray-50 px-4 sm:px-6 lg:px-8"
  >
    <div
      class="max-w-md w-full space-y-8 bg-white p-8 rounded-xl shadow-md border border-gray-100"
    >
      <div>
        <h2 class="text-center text-3xl font-extrabold text-gray-900">
          Sign In
        </h2>
        <p class="mt-2 text-center text-sm text-gray-600">
          Welcome back to Asset Tracker
        </p>
      </div>
      <div
        v-if="generalError"
        class="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-md text-sm"
      >
        {{ generalError }}
      </div>
      <form class="mt-8 space-y-5" @submit.prevent="handleLogin">
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1"
            >Email</label
          >
          <input
            v-model="credentials.email"
            type="text"
            placeholder="john@company.com"
            :class="[
              'w-full px-3 py-2 border rounded-lg shadow-sm placeholder-gray-400 focus:outline-none sm:text-sm transition-colors',
              fieldErrors.email
                ? 'border-red-500 focus:ring-2 focus:ring-red-500'
                : 'border-gray-300 focus:ring-2 focus:ring-blue-500 focus:border-blue-500',
            ]"
          />
          <p
            v-if="fieldErrors.email"
            class="mt-1 text-xs text-red-600 font-medium"
          >
            {{ fieldErrors.email }}
          </p>
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1"
            >Password</label
          >
          <input
            v-model="credentials.password"
            type="password"
            placeholder="••••••••"
            :class="[
              'w-full px-3 py-2 border rounded-lg shadow-sm placeholder-gray-400 focus:outline-none sm:text-sm transition-colors',
              fieldErrors.password
                ? 'border-red-500 focus:ring-2 focus:ring-red-500'
                : 'border-gray-300 focus:ring-2 focus:ring-blue-500 focus:border-blue-500',
            ]"
          />
          <p
            v-if="fieldErrors.password"
            class="mt-1 text-xs text-red-600 font-medium"
          >
            {{ fieldErrors.password }}
          </p>
        </div>
        <button
          type="submit"
          :disabled="isLoading"
          class="w-full flex justify-center py-2.5 px-4 border border-transparent rounded-lg shadow-sm text-sm font-medium text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 disabled:opacity-50 disabled:cursor-not-allowed transition-colors cursor-pointer"
        >
          {{ isLoading ? 'Signing in...' : 'Sign In' }}
        </button>
      </form>
      <div class="text-center text-sm text-gray-600">
        Don't have an account?
        <RouterLink
          to="/register"
          class="font-semibold text-blue-600 hover:text-blue-500"
          >Sign up</RouterLink
        >
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useRouter, RouterLink } from 'vue-router';
import { useAuthStore } from '@/stores/auth';
import type { AuthRequest } from '@/types';

const authStore = useAuthStore();
const router = useRouter();

const credentials = ref<AuthRequest>({
  email: '',
  password: '',
});

const fieldErrors = ref<Record<string, string>>({});
const generalError = ref('');
const isLoading = ref(false);

const handleLogin = async () => {
  fieldErrors.value = {};
  generalError.value = '';
  isLoading.value = true;

  try {
    await authStore.login(credentials.value);
    router.push({ name: 'Dashboard' });
  } catch (err: any) {
    const data = err.response?.data;

    if (data?.errors && typeof data.errors === 'object') {
      fieldErrors.value = data.errors;
    } else if (data?.message) {
      generalError.value = data.message;
    } else {
      generalError.value = 'An unexpected error occurred.';
    }
  } finally {
    isLoading.value = false;
  }
};
</script>
