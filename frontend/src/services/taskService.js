
import api from './api';

export const taskService = {
  async getAllTasks(params = {}) {
    const response = await api.get('/tasks', { params });
    return response.data;
  },

  async getTaskById(id) {
    const response = await api.get(`/tasks/${id}`);
    return response.data;
  },

  async createTask(task) {
    const response = await api.post('/tasks', task);
    return response.data;
  },

  async updateTask(id, task) {
    const response = await api.put(`/tasks/${id}`, task);
    return response.data;
  },

  async deleteTask(id) {
    const response = await api.delete(`/tasks/${id}`);
    return response.data;
  },

  async getTaskStats() {
    const response = await api.get('/tasks/stats');
    return response.data;
  },

  async updateStatus(id, status) {
    const response = await api.patch(`/tasks/${id}/status?status=${status}`);
    return response.data;
  },

  async searchTasks(keyword) {
    const response = await api.get(`/tasks/search?keyword=${keyword}`);
    return response.data;
  }
};
