import axios, { InternalAxiosRequestConfig, AxiosResponse } from 'axios';

const isDevelopment = process.env.NODE_ENV === 'development';
const API_URL = isDevelopment ? 'http://localhost:8080/api' : '/api';

// Интерфейсы для запросов аутентификации
interface LoginRequest {
  email: string;
  password: string;
}

interface RegisterRequest {
  email: string;
  login: string;
  password: string;
}

// Интерфейсы для ответов
interface AuthResponse {
  token: string;
  user: {
    id: string;
    email: string;
    name: string;
  };
}

// Интерфейс для данных чата
interface ChatData {
  id: string;
  title: string;
  lastMessage: string;
  date: string;
}

// Интерфейс для сообщения
interface Message {
  id: string;
  content: string;
  sender: 'user' | 'ai';
  timestamp: string;
}

// Тип для колбэка прогресса загрузки
type ProgressCallback = (progress: number) => void;

// Создаем экземпляр axios
const api = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Перехватчик для добавления токена аутентификации
api.interceptors.request.use((config: InternalAxiosRequestConfig) => {
  const token = localStorage.getItem('token');

  if (token && config.headers) {
    config.headers.Authorization = `Bearer ${token}`;
  }

  return config;
});

// Перехватчик для обработки ошибок
api.interceptors.response.use(
  (response) => response,
  (error) => {
    // Логирование ошибок
    if (error.response) {
      // Ошибка с ответом от сервера
      console.error('API Error Response:', error.response.status, error.response.data);

      // Если 401 Unauthorized, возможно, истек токен
      if (error.response.status === 401) {
        localStorage.removeItem('token');
        // Можно добавить редирект на страницу входа или показать уведомление
      }

      // Преобразуем ответ сервера в понятное сообщение об ошибке
      const errorMessage = error.response.data && error.response.data.message
        ? error.response.data.message
        : `Ошибка сервера: ${error.response.status}`;

      return Promise.reject(new Error(errorMessage));
    } else if (error.request) {
      // Запрос был сделан, но ответ не получен
      console.error('API Error Request:', error.request);
      return Promise.reject(new Error('Сервер не отвечает. Проверьте подключение к интернету.'));
    } else {
      // Ошибка при настройке запроса
      console.error('API Error Setup:', error.message);
      return Promise.reject(new Error('Произошла ошибка при выполнении запроса.'));
    }
  }
);

// API сервис
const apiService = {
  // Аутентификация
  auth: {
    // Вход в систему
    login: async (email: string, password: string): Promise<AuthResponse> => {
      try {
        const response: AxiosResponse<AuthResponse> = await api.post('/auth/login', {
          email,
          password,
        } as LoginRequest);

        return response.data;
      } catch (error) {
        throw error;
      }
    },

    // Регистрация
    register: async (email: string, login: string, password: string): Promise<AuthResponse> => {
      try {
        const response: AxiosResponse<AuthResponse> = await api.post('/auth/register', {
          email,
          login,
          password,
        } as RegisterRequest);

        return response.data;
      } catch (error) {
        throw error;
      }
    },

    // Выход из системы
    logout: async (): Promise<void> => {
      try {
        await api.post('/auth/logout');
        localStorage.removeItem('token');
      } catch (error) {
        throw error;
      }
    },

    // Проверка валидности токена
    validateToken: async (token: string): Promise<boolean> => {
      try {
        const response: AxiosResponse<{ valid: boolean }> = await api.post('/auth/validate', { token });
        return response.data.valid;
      } catch (error) {
        return false;
      }
    },
  },

  // Чаты
  chats: {
    // Получение списка чатов
    getChats: async (): Promise<ChatData[]> => {
      try {
        const response: AxiosResponse<ChatData[]> = await api.get('/chats');
        return response.data;
      } catch (error) {
        throw error;
      }
    },

    // Создание нового чата
    createChat: async (title: string): Promise<ChatData> => {
      try {
        const response: AxiosResponse<ChatData> = await api.post('/chats', { title });
        return response.data;
      } catch (error) {
        throw error;
      }
    },

    // Получение чата по ID
    getChat: async (chatId: string): Promise<ChatData> => {
      try {
        const response: AxiosResponse<ChatData> = await api.get(`/chats/${chatId}`);
        return response.data;
      } catch (error) {
        throw error;
      }
    },

    // Обновление чата
    updateChat: async (chatId: string, title: string): Promise<ChatData> => {
      try {
        const response: AxiosResponse<ChatData> = await api.put(`/chats/${chatId}`, { title });
        return response.data;
      } catch (error) {
        throw error;
      }
    },

    // Удаление чата
    deleteChat: async (chatId: string): Promise<void> => {
      try {
        await api.delete(`/chats/${chatId}`);
      } catch (error) {
        throw error;
      }
    },
  },

  // Сообщения
  messages: {
    // Получение сообщений чата
    getMessages: async (chatId: string): Promise<Message[]> => {
      try {
        const response: AxiosResponse<Message[]> = await api.get(`/chats/${chatId}/messages`);
        return response.data;
      } catch (error) {
        throw error;
      }
    },

    // Отправка сообщения
    sendMessage: async (chatId: string, content: string): Promise<Message> => {
      try {
        const response: AxiosResponse<Message> = await api.post(`/chats/${chatId}/messages`, {
          content,
        });
        return response.data;
      } catch (error) {
        throw error;
      }
    },

    // Удаление сообщения
    deleteMessage: async (chatId: string, messageId: string): Promise<void> => {
      try {
        await api.delete(`/chats/${chatId}/messages/${messageId}`);
      } catch (error) {
        throw error;
      }
    },
  },

  // Документы
  documents: {
    // Получение сгенерированного договора
    getDocument: async (chatId: string, onProgress?: ProgressCallback): Promise<Blob> => {
      try {
        const response: AxiosResponse<Blob> = await api.get(`/chats/${chatId}/document`, {
          responseType: 'blob',
          onDownloadProgress: (progressEvent) => {
            if (onProgress && progressEvent.total) {
              const percentCompleted = Math.round((progressEvent.loaded * 100) / progressEvent.total);
              onProgress(percentCompleted);
            }
          },
        });
        return response.data;
      } catch (error) {
        throw error;
      }
    },

    // Генерация нового документа
    generateDocument: async (chatId: string): Promise<void> => {
      try {
        await api.post(`/chats/${chatId}/document/generate`);
      } catch (error) {
        throw error;
      }
    },
  },

  // Пользователь
  user: {
    // Получение профиля пользователя
    getProfile: async (): Promise<{ id: string; name: string; email: string }> => {
      try {
        const response: AxiosResponse<{ id: string; name: string; email: string }> = await api.get('/user/profile');
        return response.data;
      } catch (error) {
        throw error;
      }
    },

    // Обновление профиля пользователя
    updateProfile: async (name: string): Promise<{ id: string; name: string; email: string }> => {
      try {
        const response: AxiosResponse<{ id: string; name: string; email: string }> = await api.put('/user/profile', { name });
        return response.data;
      } catch (error) {
        throw error;
      }
    },

    // Изменение пароля
    changePassword: async (currentPassword: string, newPassword: string): Promise<void> => {
      try {
        await api.post('/user/change-password', { currentPassword, newPassword });
      } catch (error) {
        throw error;
      }
    },
  },
};

export default apiService;