import axios from 'axios';
import { GuestResponse } from './guests';
import { RoomResponse } from './rooms';

const API_BASE_URL = 'http://localhost:8080/api';

export enum BookingStatus {
  RESERVED = 'RESERVED',
  CHECKED_IN = 'CHECKED_IN',
  CHECKED_OUT = 'CHECKED_OUT',
  CANCELLED = 'CANCELLED'
}

export interface BookingRequest {
  guestId: number;
  roomId: number;
  checkInDate: string;
  checkOutDate: string;
  numberOfAdults: number;
  numberOfChildren?: number;
}

export interface BookingResponse {
  id: number;
  checkInDate: string;
  checkOutDate: string;
  totalAmount: number;
  status: BookingStatus;
  guest: GuestResponse;
  room: RoomResponse;
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

const formatDateTime = (dateString: string): string => {
  if (!dateString) return '';
  const date = new Date(dateString);
  return date.toISOString();
};

export const bookingsAPI = {
  createBooking: async (data: BookingRequest): Promise<BookingResponse> => {
    const payload = {
      ...data,
      checkInDate: formatDateTime(data.checkInDate),
      checkOutDate: formatDateTime(data.checkOutDate),
    };
    const response = await api.post('/bookings', payload);
    return response.data;
  },

  getAllBookings: async (pageNo: number = 0, pageSize: number = 10): Promise<BookingResponse[]> => {
    const response = await api.get('/bookings', {
      params: { pageNo, pageSize }
    });
    return response.data;
  },

  getBookingById: async (id: number): Promise<BookingResponse> => {
    const response = await api.get(`/bookings/${id}`);
    return response.data;
  },

  getBookingsByGuest: async (guestId: number, pageNo: number = 0, pageSize: number = 10): Promise<BookingResponse[]> => {
    const response = await api.get(`/bookings/guest/${guestId}`, {
      params: { pageNo, pageSize }
    });
    return response.data;
  },

  updateBooking: async (id: number, data: BookingRequest): Promise<BookingResponse> => {
    const payload = {
      ...data,
      checkInDate: formatDateTime(data.checkInDate),
      checkOutDate: formatDateTime(data.checkOutDate),
    };
    const response = await api.put(`/bookings/${id}`, payload);
    return response.data;
  },

  deleteBooking: async (id: number): Promise<string> => {
    const response = await api.delete(`/bookings/${id}`);
    return response.data;
  },

  checkIn: async (id: number): Promise<string> => {
    const response = await api.patch(`/bookings/${id}/check-in`);
    return response.data;
  },

  checkOut: async (id: number): Promise<string> => {
    const response = await api.patch(`/bookings/${id}/check-out`);
    return response.data;
  },

  cancelBooking: async (id: number): Promise<string> => {
    const response = await api.patch(`/bookings/${id}/cancel`);
    return response.data;
  },
};
