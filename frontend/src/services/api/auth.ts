import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

export interface LoginRequest {
  username: string;
  password: string;
}

export interface RegisterRequest {
  username: string;
  password: string;
}

export interface StaffRequest {
  username: string;
  password?: string;
  email: string;
  firstName: string;
  lastName: string;
  role: 'ROLE_USER' | 'ROLE_RECEPTIONIST' | 'ROLE_MANAGER';
}

export interface AuthResponse {
  token: string;
  username: string;
  role: 'ROLE_USER' | 'ROLE_RECEPTIONIST' | 'ROLE_MANAGER';
}

export interface User {
  id: number;
  username: string;
  email: string;
  firstName: string;
  lastName: string;
  role: 'ROLE_USER' | 'ROLE_RECEPTIONIST' | 'ROLE_MANAGER';
}

const api = axios.create({
  baseURL: API_BASE_URL,
});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export const authAPI = {
  login: async (data: LoginRequest): Promise<AuthResponse> => {
    const response = await api.post('/auth/login', data);
    return response.data;
  },

  register: async (data: RegisterRequest): Promise<AuthResponse> => {
    const response = await api.post('/auth/register', data);
    return response.data;
  },

  createStaff: async (data: StaffRequest): Promise<string> => {
    const response = await api.post('/auth/create-staff', data);
    return response.data;
  },

  getAllStaff: async (): Promise<User[]> => {
    const response = await api.get('/auth/staff');
    return response.data;
  },

  updateStaff: async (id: number, data: StaffRequest): Promise<string> => {
    const response = await api.put(`/auth/staff/${id}`, data);
    return response.data;
  },

  deleteStaff: async (id: number): Promise<string> => {
    const response = await api.delete(`/auth/staff/${id}`);
    return response.data;
  },
};
