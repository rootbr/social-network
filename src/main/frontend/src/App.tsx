import React, { useState, useEffect } from 'react';
import AuthPage, { AuthPageProps } from './components/AuthPage';
import Dashboard, { DashboardProps, ChatItem } from './components/Dashboard';
import ChatComponent, { ChatComponentProps, ChatData } from './components/ChatComponent';
import apiService from './services/apiService';

// Тип для различных экранов приложения
type Screen = 'auth' | 'dashboard' | 'chat';

function App() {
  // Состояние для отслеживания текущего экрана
  const [currentScreen, setCurrentScreen] = useState<Screen>('auth');

  // Информация о JWT токене
  const [token, setToken] = useState<string | null>(null);

  // Информация о текущем открытом чате
  const [currentChat, setCurrentChat] = useState<ChatData | null>(null);

  // Состояние загрузки
  const [isLoading, setIsLoading] = useState<boolean>(true);

  // При первом рендере проверяем наличие токена в localStorage
  useEffect(() => {
    const checkAuth = async () => {
      setIsLoading(true);
      const savedToken = localStorage.getItem('token');

      if (savedToken) {
        try {
          // Проверяем валидность токена на сервере
          const isValid = await apiService.auth.validateToken(savedToken);

          if (isValid) {
            setToken(savedToken);
            setCurrentScreen('dashboard');
          } else {
            // Если токен недействителен, удаляем его
            localStorage.removeItem('token');
            setCurrentScreen('auth');
          }
        } catch (error) {
          console.error('Token validation failed:', error);
          // При любой ошибке удаляем токен и показываем экран входа
          localStorage.removeItem('token');
          setCurrentScreen('auth');
        }
      } else {
        // Если токена нет, переходим на страницу авторизации
        setCurrentScreen('auth');
      }

      setIsLoading(false);
    };

    checkAuth();
  }, []);

  // Обработчик успешной авторизации
  const handleLogin = (receivedToken: string) => {
    setToken(receivedToken);
    localStorage.setItem('token', receivedToken);
    setCurrentScreen('dashboard');
  };

  // Обработчик регистрации
  const handleRegister = () => {
    // После успешной регистрации переключаемся на экран входа
    setCurrentScreen('auth');
  };

  // Обработчик выхода из системы
  const handleLogout = () => {
    setToken(null);
    localStorage.removeItem('token');
    setCurrentScreen('auth');
  };

  // Обработчик открытия чата
  const handleOpenChat = (chat: ChatItem) => {
    setCurrentChat(chat);
    setCurrentScreen('chat');
  };

  // Обработчик создания нового чата
  const handleCreateNewChat = () => {
    // Создаем новый чат через API
    apiService.chats.createChat('Новый договор аренды')
      .then(newChat => {
        setCurrentChat(newChat);
        setCurrentScreen('chat');
      })
      .catch(error => {
        console.error('Failed to create new chat:', error);
        alert('Не удалось создать новый чат. Пожалуйста, попробуйте позже.');
      });
  };

  // Обработчик возврата к списку чатов
  const handleBackToDashboard = () => {
    setCurrentScreen('dashboard');
  };

  // Если приложение загружается, показываем индикатор загрузки
  if (isLoading) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-amber-50">
        <div className="text-center">
          <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-amber-600 mx-auto"></div>
          <p className="mt-4 text-amber-800 font-medium">Загрузка приложения...</p>
        </div>
      </div>
    );
  }

  // Отображаем соответствующий экран
  const renderScreen = () => {
    switch (currentScreen) {
      case 'auth':
        return (
          <AuthPage
            onLogin={handleLogin}
            onRegister={handleRegister}
          />
        );
      case 'dashboard':
        return (
          <Dashboard
            onLogout={handleLogout}
            onChatSelect={handleOpenChat}
            onCreateNewChat={handleCreateNewChat}
          />
        );
      case 'chat':
        return (
          <ChatComponent
            chat={currentChat}
            onBack={handleBackToDashboard}
          />
        );
      default:
        return <AuthPage onLogin={handleLogin} onRegister={handleRegister} />;
    }
  };

  return (
    <div className="app">
      {renderScreen()}
    </div>
  );
}

export default App;