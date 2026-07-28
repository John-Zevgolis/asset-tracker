<template>
  <Layout>
    <main class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 w-full">
      <div
        class="flex flex-col min-[992px]:flex-row justify-between items-start min-[992px]:items-center gap-4 mb-6"
      >
        <div>
          <h1 class="text-2xl font-bold text-gray-900">Employees Management</h1>
          <p class="text-sm text-gray-500">
            Manage company personnel and department assignments
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
          Add New Employee
        </button>
      </div>
      <div
        v-if="generalError"
        class="mb-6 p-4 bg-red-50 border border-red-200 text-red-700 rounded-md text-sm"
      >
        {{ generalError }}
      </div>
      <div v-if="isLoading" class="text-center py-12 text-gray-500">
        Loading employees...
      </div>
      <div v-else>
        <div
          v-if="employees.length === 0"
          class="bg-white rounded-xl shadow-sm border border-gray-200 p-8 text-center text-gray-400"
        >
          No employees found. Click "Add New Employee" to create one.
        </div>
        <div v-else class="grid grid-cols-1 gap-4 min-[992px]:hidden">
          <div
            v-for="employee in employees"
            :key="employee.id"
            class="bg-white rounded-xl border border-gray-200 p-4 shadow-sm space-y-3"
          >
            <div class="flex justify-between items-start">
              <div>
                <h3 class="font-semibold text-gray-900 text-base">
                  {{ employee.fullName }}
                </h3>
                <p class="text-xs text-gray-500 mt-0.5">
                  {{ employee.email }}
                </p>
              </div>
              <span
                class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-blue-50 text-blue-700 border border-blue-100"
              >
                {{ employee.department }}
              </span>
            </div>
            <div
              class="flex items-center justify-between pt-2 border-t border-gray-100"
            >
              <span
                class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-semibold bg-gray-100 text-gray-800"
              >
                {{ employee.assets?.length || 0 }} assets
              </span>
              <div
                class="flex items-center space-x-1 bg-gray-50 p-1 rounded-lg border border-gray-100"
              >
                <button
                  @click="openEditModal(employee)"
                  title="Edit Employee"
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
                  @click="handleDelete(employee.id)"
                  title="Delete Employee"
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
                  <th class="py-3.5 px-4">Full Name</th>
                  <th class="py-3.5 px-4">Email</th>
                  <th class="py-3.5 px-4">Department</th>
                  <th class="py-3.5 px-4">Assigned Assets</th>
                  <th class="py-3.5 px-4 text-center">Actions</th>
                </tr>
              </thead>
              <tbody class="divide-y divide-gray-200 text-sm">
                <tr
                  v-for="employee in employees"
                  :key="employee.id"
                  class="hover:bg-gray-50/80 transition-colors group"
                >
                  <td class="py-3.5 px-4 font-medium text-gray-900">
                    {{ employee.fullName }}
                  </td>
                  <td class="py-3.5 px-4 text-gray-600">
                    {{ employee.email }}
                  </td>
                  <td class="py-3.5 px-4">
                    <span
                      class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-blue-50 text-blue-700 border border-blue-100"
                    >
                      {{ employee.department }}
                    </span>
                  </td>
                  <td class="py-3.5 px-4">
                    <span
                      class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-semibold bg-gray-100 text-gray-800"
                    >
                      {{ employee.assets?.length || 0 }} assets
                    </span>
                  </td>
                  <td class="py-3.5 px-4 text-center">
                    <div
                      class="inline-flex items-center justify-center space-x-1 bg-gray-50 p-1 rounded-lg border border-gray-100"
                    >
                      <button
                        @click="openEditModal(employee)"
                        title="Edit Employee"
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
                        @click="handleDelete(employee.id)"
                        title="Delete Employee"
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
            {{ isEditing ? 'Edit Employee' : 'Add New Employee' }}
          </h2>
          <form @submit.prevent.stop="handleSubmit" class="space-y-4">
            <div>
              <label class="block text-xs font-semibold text-gray-700 mb-1"
                >Full Name</label
              >
              <input
                v-model="employeeForm.fullName"
                type="text"
                placeholder="e.g. John Doe"
                :class="[
                  'w-full px-3 py-2 border rounded-lg text-sm focus:outline-none',
                  fieldErrors.fullName
                    ? 'border-red-500'
                    : 'border-gray-300 focus:border-blue-500',
                ]"
              />
              <p v-if="fieldErrors.fullName" class="mt-1 text-xs text-red-600">
                {{ fieldErrors.fullName }}
              </p>
            </div>
            <div>
              <label class="block text-xs font-semibold text-gray-700 mb-1"
                >Email</label
              >
              <input
                v-model="employeeForm.email"
                type="email"
                placeholder="e.g. john.doe@company.com"
                :class="[
                  'w-full px-3 py-2 border rounded-lg text-sm focus:outline-none',
                  fieldErrors.email
                    ? 'border-red-500'
                    : 'border-gray-300 focus:border-blue-500',
                ]"
              />
              <p v-if="fieldErrors.email" class="mt-1 text-xs text-red-600">
                {{ fieldErrors.email }}
              </p>
            </div>
            <div>
              <label class="block text-xs font-semibold text-gray-700 mb-1"
                >Department</label
              >
              <select
                v-model="employeeForm.department"
                class="w-full px-3 py-2 border border-gray-300 rounded-lg text-sm focus:outline-none focus:border-blue-500 bg-white"
              >
                <option v-for="dept in departments" :key="dept" :value="dept">
                  {{ dept }}
                </option>
              </select>
              <p
                v-if="fieldErrors.department"
                class="mt-1 text-xs text-red-600"
              >
                {{ fieldErrors.department }}
              </p>
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
                      ? 'Update Employee'
                      : 'Save Employee'
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
import type { EmployeeResponse, EmployeeRequest, Department } from '@/types';

const employees = ref<EmployeeResponse[]>([]);
const departments = ref<Department[]>([]);
const isLoading = ref(true);
const isSubmitting = ref(false);

const showModal = ref(false);
const isEditing = ref(false);
const currentEditingId = ref<number | null>(null);

const employeeForm = ref<EmployeeRequest>({
  fullName: '',
  email: '',
  department: 'IT',
});

const fieldErrors = ref<Record<string, string>>({});
const generalError = ref('');

const fetchData = async () => {
  isLoading.value = true;
  generalError.value = '';
  try {
    const [employeesRes, departmentsRes] = await Promise.all([
      api.get<EmployeeResponse[]>('/employees'),
      api.get<Department[]>('/employees/departments'),
    ]);

    employees.value = employeesRes.data;
    departments.value = departmentsRes.data;

    if (departments.value.length > 0 && !employeeForm.value.department) {
      employeeForm.value.department = departments.value[0];
    }
  } catch (err: any) {
    generalError.value =
      err.response?.data?.message || 'Failed to load employees data.';
  } finally {
    isLoading.value = false;
  }
};

const openAddModal = () => {
  isEditing.value = false;
  currentEditingId.value = null;
  fieldErrors.value = {};
  employeeForm.value = {
    fullName: '',
    email: '',
    department: departments.value[0] || 'IT',
  };
  showModal.value = true;
};

const openEditModal = (employee: EmployeeResponse) => {
  isEditing.value = true;
  currentEditingId.value = employee.id;
  fieldErrors.value = {};
  employeeForm.value = {
    fullName: employee.fullName,
    email: employee.email,
    department: employee.department,
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
      await api.put(`/employees/${currentEditingId.value}`, employeeForm.value);
    } else {
      await api.post('/employees', employeeForm.value);
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
      generalError.value = 'Failed to save employee.';
    }
  } finally {
    isSubmitting.value = false;
  }
};

const handleDelete = async (id: number) => {
  if (!confirm('Are you sure you want to delete this employee?')) return;

  try {
    await api.delete(`/employees/${id}`);
    await fetchData();
  } catch (err: any) {
    alert(err.response?.data?.message || 'Failed to delete employee.');
  }
};

onMounted(() => {
  fetchData();
});
</script>
