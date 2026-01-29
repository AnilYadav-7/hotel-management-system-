import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

export enum RoomType {
  SINGLE = 'SINGLE',
  DOUBLE = 'DOUBLE',
  SUITE = 'SUITE',
  DELUXE = 'DELUXE'
}

export enum RoomStatus {
  AVAILABLE = 'AVAILABLE',
  BOOKED = 'BOOKED',
  MAINTENANCE = 'MAINTENANCE'
}

export interface RoomRequest {
  roomNumber: string;
  roomType: RoomType;
  price: number;
}

export interface RoomResponse {
  id: number;
  roomNumber: string;
  roomType: RoomType;
  price: number;
  status: RoomStatus;
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

export const roomsAPI = {
  createRoom: async (data: RoomRequest): Promise<RoomResponse> => {
    const response = await api.post('/rooms', data);
    return response.data;
  },

  getAllRooms: async (pageNo: number = 0, pageSize: number = 10): Promise<RoomResponse[]> => {
    const response = await api.get('/rooms', {
      params: { pageNo, pageSize }
    });
    return response.data;
  },

  getAvailableRooms: async (pageNo: number = 0, pageSize: number = 10): Promise<RoomResponse[]> => {
    const response = await api.get('/rooms/available', {
      params: { pageNo, pageSize }
    });
    return response.data;
  },

  getRoomById: async (id: number): Promise<RoomResponse> => {
    const response = await api.get(`/rooms/${id}`);
    return response.data;
  },

  updateRoom: async (id: number, data: RoomRequest): Promise<RoomResponse> => {
    const response = await api.put(`/rooms/${id}`, data);
    return response.data;
  },

  deleteRoom: async (id: number): Promise<string> => {
    const response = await api.delete(`/rooms/${id}`);
    return response.data;
  },
};
