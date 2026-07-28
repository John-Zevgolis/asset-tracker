import axios, {
  type InternalAxiosRequestConfig,
  type AxiosResponse,
} from 'axios';
import router from '@/router';

const api = axios.create({
  baseURL: 'https://asset-tracker-api.up.railway.app/api',
  headers: {
    'Content-Type': 'application/json',
  },
});

api.interceptors.request.use(
  (config: InternalAxiosRequestConfig): InternalAxiosRequestConfig => {
    const token = localStorage.getItem('token');
    if (token && config.headers) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error: unknown) => {
    return Promise.reject(error);
  },
);

api.interceptors.response.use(
  (response: AxiosResponse) => response,
  (error: unknown) => {
    if (axios.isAxiosError(error) && error.response?.status === 401) {
      const isLoginRequest = error.config?.url?.includes('/auth/login');

      if (!isLoginRequest) {
        localStorage.removeItem('token');
        router.push({ name: 'Login' });
      }
    }
    return Promise.reject(error);
  },
);

export default api;
