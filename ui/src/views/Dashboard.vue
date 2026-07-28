<template>
  <Layout>
    <main class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 w-full">
      <h1 class="text-2xl font-bold text-gray-900 mb-6">Dashboard Overview</h1>

      <div v-if="isLoading" class="text-center py-12 text-gray-500">
        Loading statistics...
      </div>

      <div
        v-else-if="errorMessage"
        class="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-md"
      >
        {{ errorMessage }}
      </div>
      <div
        v-else-if="stats"
        class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6"
      >
        <div class="bg-white p-6 rounded-xl border border-gray-200 shadow-sm">
          <p
            class="text-xs font-semibold text-gray-500 uppercase tracking-wider"
          >
            Total Assets
          </p>
          <p class="mt-2 text-3xl font-extrabold text-gray-900">
            {{ stats.totalAssets }}
          </p>
          <p class="mt-1 text-xs text-gray-400">Registered items</p>
        </div>
        <div
          class="bg-white p-6 rounded-xl border border-gray-200 shadow-sm border-l-4 border-l-blue-600"
        >
          <p
            class="text-xs font-semibold text-gray-500 uppercase tracking-wider"
          >
            Total Value
          </p>
          <p class="mt-2 text-3xl font-extrabold text-gray-900">
            {{ formatCurrency(stats.totalAssetsValue) }}
          </p>
          <p class="mt-1 text-xs text-gray-400">Estimated equipment value</p>
        </div>
        <div class="bg-white p-6 rounded-xl border border-gray-200 shadow-sm">
          <p
            class="text-xs font-semibold text-gray-500 uppercase tracking-wider mb-3"
          >
            Assignment Status
          </p>
          <div class="flex flex-col gap-2">
            <span
              class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-semibold bg-green-100 text-green-800 w-fit"
            >
              {{ stats.assignedAssetsCount }} Assigned
            </span>
            <span
              class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-semibold bg-amber-100 text-amber-800 w-fit"
            >
              {{ stats.unassignedAssetsCount }} Unassigned
            </span>
          </div>
        </div>
        <div class="bg-white p-6 rounded-xl border border-gray-200 shadow-sm">
          <p
            class="text-xs font-semibold text-gray-500 uppercase tracking-wider"
          >
            Total Employees
          </p>
          <p class="mt-2 text-3xl font-extrabold text-gray-900">
            {{ stats.totalEmployees }}
          </p>
          <p class="mt-1 text-xs text-gray-400">Active workforce</p>
        </div>
      </div>
    </main>
  </Layout>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import api from '@/api/axios';
import type { DashboardStats } from '@/types';
import Layout from '@/layouts/Layout.vue';

const stats = ref<DashboardStats | null>(null);
const isLoading = ref(true);
const errorMessage = ref('');

const fetchStats = async () => {
  try {
    const response = await api.get<DashboardStats>('/dashboard/stats');
    stats.value = response.data;
  } catch (err: any) {
    errorMessage.value = 'Failed to load dashboard statistics.';
  } finally {
    isLoading.value = false;
  }
};

const formatCurrency = (amount: number) => {
  return new Intl.NumberFormat('en-US', {
    style: 'currency',
    currency: 'EUR',
  }).format(amount || 0);
};

onMounted(() => {
  fetchStats();
});
</script>
