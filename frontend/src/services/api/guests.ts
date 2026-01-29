import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

export interface GuestRequest {
  name: string;
  email: string;
  phoneNumber: string;
  idProofNumber: string;
  address: string;
}

export interface GuestResponse {
  id: number;
  name: string;
  email: string;
  phoneNumber: string;
  idProofNumber: string;
  address: string;
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

export const guestsAPI = {
  createGuest: async (data: GuestRequest): Promise<GuestResponse> => {
    const response = await api.post('/guests', data);
    return response.data;
  },

  getAllGuests: async (pageNo: number = 0, pageSize: number = 10): Promise<GuestResponse[]> => {
    const response = await api.get('/guests', {
      params: { pageNo, pageSize }
    });
    return response.data;
  },

  getGuestById: async (id: number): Promise<GuestResponse> => {
    const response = await api.get(`/guests/${id}`);
    return response.data;
  },

  getGuestByPhone: async (phone: string): Promise<GuestResponse> => {
    const response = await api.get(`/guests/phone/${phone}`);
    return response.data;
  },

  updateGuest: async (id: number, data: GuestRequest): Promise<GuestResponse> => {
    const response = await api.put(`/guests/${id}`, data);
    return response.data;
  },

  deleteGuest: async (id: number): Promise<string> => {
    const response = await api.delete(`/guests/${id}`);
    return response.data;
  },
};
