<template>
  <Layout>
    <main class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 w-full">
      <div
        class="flex flex-col min-[992px]:flex-row justify-between items-start min-[992px]:items-center gap-4 mb-6"
      >
        <div>
          <h1 class="text-2xl font-bold text-gray-900">Assets Management</h1>
          <p class="text-sm text-gray-500">
            Track and assign company equipment
          </p>
        </div>
        <button
          @click="openAddModal"
          class="w-full min-[992px]:w-auto px-4 py-2 bg-blue-600 hover:bg-blue-700 text-white font-medium text-sm rounded-lg shadow-sm transition-colors flex items-center justify-center gap-2 cursor-pointer"
        >
          <svg
            class="w-4 h-4"
            fill="none"
            stroke="currentColor"
            viewBox="0 0 24 24"
          >
            <path
              stroke-linecap="round"
              stroke-linejoin="round"
              stroke-width="2"
              d="M12 4v16m8-8H4"
            />
          </svg>
          Add New Asset
        </button>
      </div>
      <div
        v-if="generalError"
        class="mb-6 p-4 bg-red-50 border border-red-200 text-red-700 rounded-md text-sm"
      >
        {{ generalError }}
      </div>
      <div v-if="isLoading" class="text-center py-12 text-gray-500">
        Loading assets...
      </div>

      <div v-else>
        <div
          v-if="assets.length === 0"
          class="bg-white rounded-xl shadow-sm border border-gray-200 p-8 text-center text-gray-400"
        >
          No assets found. Click "Add New Asset" to create one.
        </div>

        <div v-else class="grid grid-cols-1 gap-4 min-[992px]:hidden">
          <div
            v-for="asset in assets"
            :key="asset.id"
            class="bg-white rounded-xl border border-gray-200 p-4 shadow-sm space-y-3"
          >
            <div class="flex justify-between items-start">
              <div>
                <h3 class="font-semibold text-gray-900 text-base">
                  {{ asset.name }}
                </h3>
                <p class="font-mono text-xs text-gray-500 mt-0.5">
                  SN: {{ asset.serialNumber }}
                </p>
              </div>
              <span
                class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-gray-100 text-gray-800 border border-gray-200"
              >
                {{ asset.category }}
              </span>
            </div>
            <div
              class="grid grid-cols-2 gap-2 text-xs py-2 border-y border-gray-100"
            >
              <div>
                <span class="text-gray-500 block">Value</span>
                <span class="font-semibold text-gray-900 text-sm">
                  {{ formatCurrency(asset.value) }}
                </span>
              </div>
              <div>
                <span class="text-gray-500 block">Purchase Date</span>
                <span class="text-gray-700">
                  {{ asset.purchaseDate }}
                </span>
              </div>
            </div>
            <div
              class="flex flex-col sm:flex-row items-stretch sm:items-end lg:items-center justify-between gap-3 pt-1"
            >
              <div class="flex-1">
                <label
                  class="block text-[10px] font-semibold uppercase text-gray-400 mb-1"
                  >Assigned To</label
                >
                <select
                  :value="asset.employeeId || ''"
                  @change="handleAssignChange(asset.id, $event.target)"
                  class="w-full text-xs border border-gray-300 rounded-lg py-1.5 px-2.5 focus:ring-2 focus:ring-blue-500 focus:outline-none bg-white shadow-xs cursor-pointer"
                >
                  <option value="">-- Unassigned --</option>
                  <option
                    v-for="emp in employees"
                    :key="emp.id"
                    :value="emp.id"
                  >
                    {{ emp.fullName }} ({{ emp.department }})
                  </option>
                </select>
              </div>
              <div
                class="flex items-center justify-end space-x-1 bg-gray-50 p-1 rounded-lg border border-gray-100 self-end sm:self-auto"
              >
                <button
                  @click="openEditModal(asset)"
                  title="Edit Asset"
                  class="p-1.5 text-gray-600 hover:text-blue-600 hover:bg-blue-50 rounded-md transition-all duration-150 cursor-pointer"
                >
                  <svg
                    class="w-4 h-4"
                    fill="none"
                    stroke="currentColor"
                    viewBox="0 0 24 24"
                  >
                    <path
                      stroke-linecap="round"
                      stroke-linejoin="round"
                      stroke-width="2"
                      d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z"
                    />
                  </svg>
                </button>
                <button
                  @click="handleDelete(asset.id)"
                  title="Delete Asset"
                  class="p-1.5 text-gray-600 hover:text-red-600 hover:bg-red-50 rounded-md transition-all duration-150 cursor-pointer"
                >
                  <svg
                    class="w-4 h-4"
                    fill="none"
                    stroke="currentColor"
                    viewBox="0 0 24 24"
                  >
                    <path
                      stroke-linecap="round"
                      stroke-linejoin="round"
                      stroke-width="2"
                      d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"
                    />
                  </svg>
                </button>
              </div>
            </div>
          </div>
        </div>
        <div
          class="hidden min-[992px]:block bg-white rounded-xl shadow-sm border border-gray-200 overflow-hidden"
        >
          <div class="overflow-x-auto">
            <table class="w-full text-left border-collapse">
              <thead>
                <tr
                  class="bg-gray-50 border-b border-gray-200 text-xs font-semibold text-gray-500 uppercase tracking-wider"
                >
                  <th class="py-3.5 px-4">Name</th>
                  <th class="py-3.5 px-4">Category</th>
                  <th class="py-3.5 px-4">Serial Number</th>
                  <th class="py-3.5 px-4">Value</th>
                  <th class="py-3.5 px-4">Purchase Date</th>
                  <th class="py-3.5 px-4">Assigned To</th>
                  <th class="py-3.5 px-4 text-center">Actions</th>
                </tr>
              </thead>
              <tbody class="divide-y divide-gray-200 text-sm">
                <tr
                  v-for="asset in assets"
                  :key="asset.id"
                  class="hover:bg-gray-50/80 transition-colors group"
                >
                  <td class="py-3.5 px-4 font-medium text-gray-900">
                    {{ asset.name }}
                  </td>
                  <td class="py-3.5 px-4">
                    <span
                      class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-gray-100 text-gray-800 border border-gray-200"
                    >
                      {{ asset.category }}
                    </span>
                  </td>
                  <td class="py-3.5 px-4 font-mono text-xs text-gray-600">
                    {{ asset.serialNumber }}
                  </td>
                  <td class="py-3.5 px-4 font-semibold text-gray-900">
                    {{ formatCurrency(asset.value) }}
                  </td>
                  <td class="py-3.5 px-4 text-gray-500">
                    {{ asset.purchaseDate }}
                  </td>
                  <td class="py-3.5 px-4">
                    <select
                      :value="asset.employeeId || ''"
                      @change="handleAssignChange(asset.id, $event.target)"
                      class="text-xs border border-gray-300 rounded-lg py-1.5 px-2.5 focus:ring-2 focus:ring-blue-500 focus:outline-none bg-white shadow-xs cursor-pointer"
                    >
                      <option value="">-- Unassigned --</option>
                      <option
                        v-for="emp in employees"
                        :key="emp.id"
                        :value="emp.id"
                      >
                        {{ emp.fullName }} ({{ emp.department }})
                      </option>
                    </select>
                  </td>
                  <td class="py-3.5 px-4 text-center">
                    <div
                      class="inline-flex items-center justify-center space-x-1 bg-gray-50 p-1 rounded-lg border border-gray-100"
                    >
                      <button
                        @click="openEditModal(asset)"
                        title="Edit Asset"
                        class="p-1.5 text-gray-600 hover:text-blue-600 hover:bg-blue-50 rounded-md transition-all duration-150 cursor-pointer"
                      >
                        <svg
                          class="w-4 h-4"
                          fill="none"
                          stroke="currentColor"
                          viewBox="0 0 24 24"
                        >
                          <path
                            stroke-linecap="round"
                            stroke-linejoin="round"
                            stroke-width="2"
                            d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z"
                          />
                        </svg>
                      </button>
                      <button
                        @click="handleDelete(asset.id)"
                        title="Delete Asset"
                        class="p-1.5 text-gray-600 hover:text-red-600 hover:bg-red-50 rounded-md transition-all duration-150 cursor-pointer"
                      >
                        <svg
                          class="w-4 h-4"
                          fill="none"
                          stroke="currentColor"
                          viewBox="0 0 24 24"
                        >
                          <path
                            stroke-linecap="round"
                            stroke-linejoin="round"
                            stroke-width="2"
                            d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"
                          />
                        </svg>
                      </button>
                    </div>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
      <div
        v-if="showModal"
        class="fixed inset-0 bg-black/50 flex items-center justify-center p-4 z-50 backdrop-blur-xs"
      >
        <div
          class="bg-white rounded-xl shadow-xl max-w-md w-full p-6 border border-gray-100"
        >
          <h2 class="text-xl font-bold text-gray-900 mb-4">
            {{ isEditing ? 'Edit Asset' : 'Add New Asset' }}
          </h2>
          <form @submit.prevent.stop="handleSubmit" class="space-y-4">
            <div>
              <label class="block text-xs font-semibold text-gray-700 mb-1"
                >Asset Name</label
              >
              <input
                v-model="assetForm.name"
                type="text"
                placeholder="e.g. MacBook Pro M3"
                :class="[
                  'w-full px-3 py-2 border rounded-lg text-sm focus:outline-none',
                  fieldErrors.name
                    ? 'border-red-500'
                    : 'border-gray-300 focus:border-blue-500',
                ]"
              />
              <p v-if="fieldErrors.name" class="mt-1 text-xs text-red-600">
                {{ fieldErrors.name }}
              </p>
            </div>
            <div>
              <label class="block text-xs font-semibold text-gray-700 mb-1"
                >Category</label
              >
              <select
                v-model="assetForm.category"
                class="w-full px-3 py-2 border border-gray-300 rounded-lg text-sm focus:outline-none focus:border-blue-500 bg-white"
              >
                <option v-for="cat in categories" :key="cat" :value="cat">
                  {{ cat }}
                </option>
              </select>
            </div>
            <div>
              <label class="block text-xs font-semibold text-gray-700 mb-1"
                >Serial Number</label
              >
              <input
                v-model="assetForm.serialNumber"
                type="text"
                placeholder="e.g. SN-99882211"
                :class="[
                  'w-full px-3 py-2 border rounded-lg text-sm focus:outline-none',
                  fieldErrors.serialNumber
                    ? 'border-red-500'
                    : 'border-gray-300 focus:border-blue-500',
                ]"
              />
              <p
                v-if="fieldErrors.serialNumber"
                class="mt-1 text-xs text-red-600"
              >
                {{ fieldErrors.serialNumber }}
              </p>
            </div>
            <div class="grid grid-cols-2 gap-3">
              <div>
                <label class="block text-xs font-semibold text-gray-700 mb-1"
                  >Value (€)</label
                >
                <input
                  v-model.number="assetForm.value"
                  type="number"
                  step="0.01"
                  :class="[
                    'w-full px-3 py-2 border rounded-lg text-sm focus:outline-none',
                    fieldErrors.value
                      ? 'border-red-500'
                      : 'border-gray-300 focus:border-blue-500',
                  ]"
                />
                <p v-if="fieldErrors.value" class="mt-1 text-xs text-red-600">
                  {{ fieldErrors.value }}
                </p>
              </div>
              <div>
                <label class="block text-xs font-semibold text-gray-700 mb-1"
                  >Purchase Date</label
                >
                <input
                  v-model="assetForm.purchaseDate"
                  type="date"
                  :max="todayDate"
                  :class="[
                    'w-full px-3 py-2 border rounded-lg text-sm focus:outline-none',
                    fieldErrors.purchaseDate
                      ? 'border-red-500'
                      : 'border-gray-300 focus:border-blue-500',
                  ]"
                />
                <p
                  v-if="fieldErrors.purchaseDate"
                  class="mt-1 text-xs text-red-600"
                >
                  {{ fieldErrors.purchaseDate }}
                </p>
              </div>
            </div>
            <div
              class="flex justify-end space-x-3 mt-6 pt-2 border-t border-gray-100"
            >
              <button
                type="button"
                @click="closeModal"
                class="px-4 py-2 text-sm font-medium text-gray-600 hover:text-gray-800 cursor-pointer"
              >
                Cancel
              </button>
              <button
                type="submit"
                @click.prevent="handleSubmit"
                :disabled="isSubmitting"
                class="px-4 py-2 bg-blue-600 hover:bg-blue-700 text-white font-medium text-sm rounded-lg shadow-sm disabled:opacity-50 cursor-pointer"
              >
                {{
                  isSubmitting
                    ? 'Saving...'
                    : isEditing
                      ? 'Update Asset'
                      : 'Save Asset'
                }}
              </button>
            </div>
          </form>
        </div>
      </div>
    </main>
  </Layout>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import api from '@/api/axios';
import Layout from '@/layouts/Layout.vue';
import type {
  AssetResponse,
  AssetRequest,
  EmployeeResponse,
  AssetCategory,
} from '@/types';

const assets = ref<AssetResponse[]>([]);
const employees = ref<EmployeeResponse[]>([]);
const categories = ref<AssetCategory[]>([]);
const isLoading = ref(true);
const isSubmitting = ref(false);

const showModal = ref(false);
const isEditing = ref(false);
const currentEditingId = ref<number | null>(null);

const todayDate = new Date().toISOString().split('T')[0];

const assetForm = ref<AssetRequest>({
  name: '',
  category: 'LAPTOP',
  serialNumber: '',
  value: 0,
  purchaseDate: todayDate,
});

const fieldErrors = ref<Record<string, string>>({});
const generalError = ref('');

const fetchData = async () => {
  isLoading.value = true;
  generalError.value = '';
  try {
    const [assetsRes, employeesRes, categoriesRes] = await Promise.all([
      api.get<AssetResponse[]>('/assets'),
      api.get<EmployeeResponse[]>('/employees'),
      api.get<AssetCategory[]>('/assets/categories'),
    ]);

    assets.value = assetsRes.data;
    employees.value = employeesRes.data;
    categories.value = categoriesRes.data;

    if (categories.value.length > 0 && !assetForm.value.category) {
      assetForm.value.category = categories.value[0];
    }
  } catch (err: any) {
    generalError.value =
      err.response?.data?.message || 'Failed to load assets data.';
  } finally {
    isLoading.value = false;
  }
};

const openAddModal = () => {
  isEditing.value = false;
  currentEditingId.value = null;
  fieldErrors.value = {};
  assetForm.value = {
    name: '',
    category: categories.value[0] || 'LAPTOP',
    serialNumber: '',
    value: 0,
    purchaseDate: todayDate,
  };
  showModal.value = true;
};

const openEditModal = (asset: AssetResponse) => {
  isEditing.value = true;
  currentEditingId.value = asset.id;
  fieldErrors.value = {};
  assetForm.value = {
    name: asset.name,
    category: asset.category,
    serialNumber: asset.serialNumber,
    value: asset.value,
    purchaseDate: asset.purchaseDate,
  };
  showModal.value = true;
};

const closeModal = () => {
  showModal.value = false;
  fieldErrors.value = {};
};

const handleSubmit = async () => {
  fieldErrors.value = {};
  generalError.value = '';
  isSubmitting.value = true;

  try {
    if (isEditing.value && currentEditingId.value) {
      await api.put(`/assets/${currentEditingId.value}`, assetForm.value);
    } else {
      await api.post('/assets', assetForm.value);
    }
    closeModal();
    await fetchData();
  } catch (err: any) {
    const data = err.response?.data;
    if (data?.errors && typeof data.errors === 'object') {
      fieldErrors.value = data.errors;
    } else if (data?.message) {
      generalError.value = data.message;
    } else {
      generalError.value = 'Failed to save asset.';
    }
  } finally {
    isSubmitting.value = false;
  }
};

const handleAssignChange = async (
  assetId: number,
  target: EventTarget | null,
) => {
  const selectElement = target as HTMLSelectElement;
  const selectedValue = selectElement.value;

  try {
    if (selectedValue) {
      await api.put(`/assets/${assetId}/assign`, null, {
        params: { employeeId: Number(selectedValue) },
      });
    } else {
      await api.put(`/assets/${assetId}/assign`);
    }
    await fetchData();
  } catch (err: any) {
    alert(err.response?.data?.message || 'Failed to update asset assignment.');
    await fetchData();
  }
};

const handleDelete = async (id: number) => {
  if (!confirm('Are you sure you want to delete this asset?')) return;

  try {
    await api.delete(`/assets/${id}`);
    await fetchData();
  } catch (err: any) {
    alert(err.response?.data?.message || 'Failed to delete asset.');
  }
};

const formatCurrency = (amount: number) => {
  return new Intl.NumberFormat('en-US', {
    style: 'currency',
    currency: 'EUR',
  }).format(amount || 0);
};

onMounted(() => {
  fetchData();
});
</script>
