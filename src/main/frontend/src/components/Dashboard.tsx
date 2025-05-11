import React, { useState, useEffect } from 'react';
import { MessageSquare, FileText, User, LogOut, Plus, Clock, Settings, Loader, AlertCircle } from 'lucide-react';
import apiService from '../services/apiService';

// Интерфейс для элемента чата
export interface ChatItem {
  id: string;
  title: string;
  lastMessage: string;
  date: string;
}

// Интерфейс для профиля пользователя
interface UserProfile {
  name: string;
  email: string;
}

// Интерфейс пропсов компонента Dashboard
export interface DashboardProps {
  onLogout: () => void;
  onChatSelect: (chat: ChatItem) => void;
  onCreateNewChat: () => void;
}

const Dashboard: React.FC<DashboardProps> = ({ onLogout, onChatSelect, onCreateNewChat }) => {
  // Состояния для чатов и пользователя
  const [chats, setChats] = useState<ChatItem[]>([]);
  const [user, setUser] = useState<UserProfile>({
    name: 'Загрузка...',
    email: 'загрузка...'
  });

  // Состояния для обработки загрузки и ошибок
  const [isLoading, setIsLoading] = useState(true);
  const [isCreatingChat, setIsCreatingChat] = useState(false);
  const [error, setError] = useState<string | null>(null);

  // Загрузка данных при монтировании компонента
  useEffect(() => {
    const fetchData = async () => {
      try {
        setIsLoading(true);
        setError(null);

        // Получаем список чатов и информацию о пользователе
        // Можно выполнить запросы параллельно
        const [chatsData] = await Promise.all([
          apiService.chats.getChats(),
          // Здесь может быть запрос на получение профиля пользователя
          // apiService.user.getProfile()
        ]);

        // Получаем профиль пользователя из localStorage (в реальном приложении будет запрос к API)
        const token = localStorage.getItem('token');
        if (token) {
          try {
            // Пример декодирования JWT токена для получения информации о пользователе
            // Это простой пример, в реальном приложении лучше использовать библиотеку для работы с JWT
            const base64Url = token.split('.')[1];
            const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
            const jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
              return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
            }).join(''));

            const payload = JSON.parse(jsonPayload);
            setUser({
              name: payload.name || 'Пользователь',
              email: payload.email || 'пользователь@example.com'
            });
          } catch (e) {
            console.error('Failed to decode token:', e);
            // В случае ошибки устанавливаем дефолтные значения
            setUser({
              name: 'Пользователь',
              email: 'пользователь@example.com'
            });
          }
        }

        // Обновляем список чатов
        setChats(chatsData);
      } catch (error) {
        console.error('Failed to fetch data:', error);
        setError('Не удалось загрузить данные. Пожалуйста, попробуйте позже.');

        // Устанавливаем пустой список чатов в случае ошибки
        setChats([]);
      } finally {
        setIsLoading(false);
      }
    };

    fetchData();
  }, []);

  // Создание нового чата
  const createNewChat = async () => {
    try {
      setIsCreatingChat(true);
      setError(null);

      // Создаем новый чат через API
      const newChat = await apiService.chats.createChat('Новый договор аренды');

      // Добавляем новый чат в список
      setChats(prevChats => [newChat, ...prevChats]);

      // Вызываем колбэк для перехода к новому чату
      onCreateNewChat();

      // Переходим к созданному чату
      onChatSelect(newChat);
    } catch (error) {
      console.error('Failed to create chat:', error);
      setError('Не удалось создать новый чат. Пожалуйста, попробуйте позже.');
    } finally {
      setIsCreatingChat(false);
    }
  };

  // Обработчик выхода с подтверждением
  const handleLogout = async () => {
    if (window.confirm('Вы уверены, что хотите выйти из системы?')) {
      try {
        // Вызываем API для выхода
        await apiService.auth.logout();
      } catch (error) {
        console.error('Logout error:', error);
        // Даже если произошла ошибка, мы все равно выходим
      }

      // Вызываем колбэк для выхода
      onLogout();
    }
  };

  return (
    <div className="min-h-screen bg-amber-50 flex">
      {/* Боковое меню */}
      <aside className="w-64 bg-white shadow-md p-6 space-y-6">
        <div className="flex items-center space-x-3">
          <div className="w-10 h-10 rounded-full bg-amber-200 flex items-center justify-center">
            <User className="h-6 w-6 text-amber-800" />
          </div>
          <div>
            <h3 className="font-medium text-gray-900">{user.name}</h3>
            <p className="text-xs text-gray-500">{user.email}</p>
          </div>
        </div>

        <nav className="space-y-2">
          <a className="flex items-center space-x-2 px-4 py-2 rounded-lg bg-amber-100 text-amber-800 font-medium">
            <MessageSquare className="h-5 w-5" />
            <span>Мои чаты</span>
          </a>
          <a className="flex items-center space-x-2 px-4 py-2 rounded-lg text-gray-600 hover:bg-amber-50 hover:text-amber-800 transition duration-150">
            <FileText className="h-5 w-5" />
            <span>Мои документы</span>
          </a>
          <a className="flex items-center space-x-2 px-4 py-2 rounded-lg text-gray-600 hover:bg-amber-50 hover:text-amber-800 transition duration-150">
            <Settings className="h-5 w-5" />
            <span>Настройки</span>
          </a>
        </nav>

        <div className="pt-6 mt-6 border-t border-gray-200">
          <button
            onClick={handleLogout}
            className="flex items-center space-x-2 px-4 py-2 w-full rounded-lg text-gray-600 hover:bg-red-50 hover:text-red-600 transition duration-150"
          >
            <LogOut className="h-5 w-5" />
            <span>Выйти</span>
          </button>
        </div>
      </aside>

      {/* Основной контент */}
      <main className="flex-1 p-8">
        <div className="max-w-4xl mx-auto">
          <div className="flex justify-between items-center mb-6">
            <h1 className="text-2xl font-bold text-gray-900">Мои чаты</h1>
            <button
              onClick={createNewChat}
              disabled={isCreatingChat}
              className="flex items-center space-x-2 px-4 py-2 bg-amber-600 text-white rounded-lg hover:bg-amber-700 transition duration-150 disabled:opacity-70 disabled:cursor-not-allowed"
            >
              {isCreatingChat ? (
                <>
                  <Loader className="h-5 w-5 animate-spin" />
                  <span>Создание...</span>
                </>
              ) : (
                <>
                  <Plus className="h-5 w-5" />
                  <span>Новый договор</span>
                </>
              )}
            </button>
          </div>

          {/* Сообщение об ошибке */}
          {error && (
            <div className="bg-red-100 border-l-4 border-red-500 text-red-700 p-4 rounded mb-6" role="alert">
              <div className="flex items-start">
                <div className="flex-shrink-0">
                  <AlertCircle className="h-5 w-5 text-red-500" />
                </div>
                <div className="ml-3">
                  <p className="text-sm">{error}</p>
                </div>
              </div>
            </div>
          )}

          {/* Индикатор загрузки */}
          {isLoading ? (
            <div className="flex justify-center items-center py-12">
              <Loader className="h-8 w-8 text-amber-600 animate-spin" />
              <span className="ml-2 text-amber-600 font-medium">Загрузка чатов...</span>
            </div>
          ) : (
            /* Список чатов */
            <div className="space-y-4">
              {chats.length > 0 ? (
                chats.map(chat => (
                  <div
                    key={chat.id}
                    className="bg-white p-4 rounded-lg shadow hover:shadow-md transition duration-150 cursor-pointer"
                    onClick={() => onChatSelect(chat)}
                  >
                    <div className="flex justify-between items-start">
                      <h3 className="font-medium text-lg text-gray-900">{chat.title}</h3>
                      <div className="flex items-center text-xs text-gray-500">
                        <Clock className="h-3 w-3 mr-1" />
                        {chat.date}
                      </div>
                    </div>
                    <p className="text-sm text-gray-600 mt-1">{chat.lastMessage}</p>
                  </div>
                ))
              ) : (
                <div className="text-center py-12">
                  <MessageSquare className="h-12 w-12 text-gray-300 mx-auto mb-4" />
                  <h3 className="text-lg font-medium text-gray-900">У вас пока нет чатов</h3>
                  <p className="text-gray-500 mt-1">
                    Нажмите "Новый договор" чтобы начать составление договора аренды с помощью AI
                  </p>
                </div>
              )}
            </div>
          )}
        </div>
      </main>
    </div>
  );
};

export default Dashboard;