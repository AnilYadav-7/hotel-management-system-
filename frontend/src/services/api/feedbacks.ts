import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

export interface GuestFeedbackDTO {
  feedbackId?: number;
  guestName: string;
  guestEmail: string;
  roomNumber?: string;
  rating: number;
  serviceQuality: string;
  roomCleanliness: string;
  amenitiesFeedback: string;
  overallExperience: string;
  suggestions?: string;
  createdAt?: string;
}

export interface FeedbackResponse {
  success: boolean;
  message: string;
  data: GuestFeedbackDTO | GuestFeedbackDTO[];
  count?: number;
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

export const feedbacksAPI = {
  submitFeedback: async (data: GuestFeedbackDTO): Promise<FeedbackResponse> => {
    const response = await api.post('/feedbacks/submit', data);
    return response.data;
  },

  getAllFeedbacks: async (pageNo: number = 0, pageSize: number = 10): Promise<FeedbackResponse> => {
    const response = await api.get('/feedbacks', {
      params: { pageNo, pageSize }
    });
    return response.data;
  },

  getFeedbackById: async (id: number): Promise<FeedbackResponse> => {
    const response = await api.get(`/feedbacks/${id}`);
    return response.data;
  },

  searchAndFilterFeedbacks: async (params: {
    name?: string;
    id?: number;
    roomNumber?: string;
    minRating?: number;
    maxRating?: number;
  }): Promise<FeedbackResponse> => {
    const response = await api.get('/feedbacks/search', { params });
    return response.data;
  },

  updateFeedback: async (id: number, data: GuestFeedbackDTO): Promise<FeedbackResponse> => {
    const response = await api.put(`/feedbacks/${id}`, data);
    return response.data;
  },

  deleteFeedback: async (id: number): Promise<FeedbackResponse> => {
    const response = await api.delete(`/feedbacks/${id}`);
    return response.data;
  },

  healthCheck: async (): Promise<{ status: string; message: string }> => {
    const response = await api.get('/feedbacks/health');
    return response.data;
  },
};
