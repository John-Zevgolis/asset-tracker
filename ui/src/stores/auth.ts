import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import api from '@/api/axios';
import type { AuthRequest, RegisterRequest, AuthResponse } from '@/types';

export const useAuthStore = defineStore('auth', () => {
  const token = ref<string>(localStorage.getItem('token') || '');

  const isAuthenticated = computed<boolean>(() => !!token.value);

  const login = async (credentials: AuthRequest): Promise<void> => {
    const response = await api.post<AuthResponse>('/auth/login', credentials);
    token.value = response.data.token;
    localStorage.setItem('token', token.value);
  };

  const register = async (userData: RegisterRequest): Promise<void> => {
    await api.post('/auth/register', userData);
  };

  const logout = (): void => {
    token.value = '';
    localStorage.removeItem('token');
  };

  return {
    token,
    isAuthenticated,
    login,
    register,
    logout,
  };
});
