import React, { useState } from 'react';
import { User, LogIn, Mail, Lock, Key, AlertCircle, Loader } from 'lucide-react';
import apiService from '../services/apiService';

// Интерфейс пропсов для компонента AuthPage
export interface AuthPageProps {
  onLogin: (token: string) => void;
  onRegister: () => void;
}

// Типы для формы авторизации
interface LoginFormData {
  email: string;
  password: string;
}

// Типы для формы регистрации
interface RegisterFormData {
  email: string;
  login: string;
  password: string;
  confirmPassword: string;
}

const AuthPage: React.FC<AuthPageProps> = ({ onLogin, onRegister }) => {
  // Состояние для переключения между формами входа и регистрации
  const [isLogin, setIsLogin] = useState(true);

  // Состояния для форм
  const [loginForm, setLoginForm] = useState<LoginFormData>({
    email: '',
    password: ''
  });

  const [registerForm, setRegisterForm] = useState<RegisterFormData>({
    email: '',
    login: '',
    password: '',
    confirmPassword: ''
  });

  // Состояния для обработки ошибок и загрузки
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const [successMessage, setSuccessMessage] = useState<string | null>(null);
  const [isLoading, setIsLoading] = useState(false);

  // Обработчики формы входа
  const handleLoginChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setLoginForm(prev => ({ ...prev, [name]: value }));
    // Сбрасываем сообщения об ошибках при изменении формы
    setErrorMessage(null);
  };

  const handleLoginSubmit = async () => {
    try {
      // Сбрасываем сообщения об ошибках
      setErrorMessage(null);

      // Простая валидация формы
      if (!loginForm.email || !loginForm.password) {
        setErrorMessage('Пожалуйста, заполните все поля');
        return;
      }

      // Устанавливаем состояние загрузки
      setIsLoading(true);

      // Делаем запрос к API для авторизации
      const response = await apiService.auth.login(loginForm.email, loginForm.password);

      // После успешной авторизации получаем JWT токен и вызываем callback
      onLogin(response.token);

      console.log('Login successful:', response);
    } catch (error) {
      console.error('Login failed:', error);
      // Устанавливаем сообщение об ошибке
      setErrorMessage(error instanceof Error
        ? error.message
        : 'Ошибка входа. Проверьте логин и пароль.');
    } finally {
      // В любом случае снимаем состояние загрузки
      setIsLoading(false);
    }
  };

  // Обработчики формы регистрации
  const handleRegisterChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setRegisterForm(prev => ({ ...prev, [name]: value }));
    // Сбрасываем сообщения об ошибках при изменении формы
    setErrorMessage(null);
  };

  const handleRegisterSubmit = async () => {
    try {
      // Сбрасываем сообщения об ошибках
      setErrorMessage(null);

      // Простая валидация формы
      if (!registerForm.email || !registerForm.login || !registerForm.password) {
        setErrorMessage('Пожалуйста, заполните все поля');
        return;
      }

      // Проверка на совпадение паролей
      if (registerForm.password !== registerForm.confirmPassword) {
        setErrorMessage('Пароли не совпадают');
        return;
      }

      // Устанавливаем состояние загрузки
      setIsLoading(true);

      // Делаем запрос к API для регистрации
      await apiService.auth.register(
        registerForm.email,
        registerForm.login,
        registerForm.password
      );

      // После успешной регистрации показываем сообщение об успехе
      setSuccessMessage('Регистрация успешна! Теперь вы можете войти в систему.');

      // Переключаемся на форму входа
      setIsLogin(true);

      // Вызываем callback
      onRegister();

    } catch (error) {
      console.error('Registration failed:', error);
      // Устанавливаем сообщение об ошибке
      setErrorMessage(error instanceof Error
        ? error.message
        : 'Ошибка регистрации. Возможно, такой пользователь уже существует.');
    } finally {
      // В любом случае снимаем состояние загрузки
      setIsLoading(false);
    }
  };

  // Переключение между формами
  const toggleForm = () => {
    // Сбрасываем сообщения и форму при переключении
    setErrorMessage(null);
    setSuccessMessage(null);
    setIsLogin(!isLogin);
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-amber-50">
      <div className="w-full max-w-md p-8 space-y-8 bg-white rounded-lg shadow-md">
        <div className="text-center">
          <h1 className="text-4xl font-extrabold tracking-tight text-amber-800">
            {isLogin ? "Вход в систему" : "Регистрация"}
          </h1>
          <p className="mt-2 text-gray-600">
            {isLogin ? "Войдите для доступа к личному кабинету" : "Создайте аккаунт для доступа к системе"}
          </p>
        </div>

        {/* Сообщения об ошибках и успехе */}
        {errorMessage && (
          <div className="bg-red-100 border-l-4 border-red-500 text-red-700 p-4 rounded" role="alert">
            <div className="flex items-start">
              <div className="flex-shrink-0">
                <AlertCircle className="h-5 w-5 text-red-500" />
              </div>
              <div className="ml-3">
                <p className="text-sm">{errorMessage}</p>
              </div>
            </div>
          </div>
        )}

        {successMessage && (
          <div className="bg-green-100 border-l-4 border-green-500 text-green-700 p-4 rounded" role="alert">
            <p className="text-sm">{successMessage}</p>
          </div>
        )}

        {isLogin ? (
          // Форма входа
          <div className="mt-8 space-y-6">
            <div className="space-y-4">
              <div>
                <label htmlFor="email" className="block text-sm font-medium text-gray-700">
                  Электронная почта
                </label>
                <div className="mt-1 relative rounded-md shadow-sm">
                  <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                    <Mail className="h-5 w-5 text-amber-500" />
                  </div>
                  <input
                    id="email"
                    name="email"
                    type="email"
                    required
                    value={loginForm.email}
                    onChange={handleLoginChange}
                    className="block w-full pl-10 pr-3 py-2 border border-amber-300 rounded-md focus:outline-none focus:ring-amber-500 focus:border-amber-500"
                    placeholder="example@mail.com"
                    disabled={isLoading}
                  />
                </div>
              </div>

              <div>
                <label htmlFor="password" className="block text-sm font-medium text-gray-700">
                  Пароль
                </label>
                <div className="mt-1 relative rounded-md shadow-sm">
                  <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                    <Lock className="h-5 w-5 text-amber-500" />
                  </div>
                  <input
                    id="password"
                    name="password"
                    type="password"
                    required
                    value={loginForm.password}
                    onChange={handleLoginChange}
                    className="block w-full pl-10 pr-3 py-2 border border-amber-300 rounded-md focus:outline-none focus:ring-amber-500 focus:border-amber-500"
                    placeholder="••••••••"
                    disabled={isLoading}
                  />
                </div>
              </div>
            </div>

            <div>
              <button
                onClick={handleLoginSubmit}
                disabled={isLoading}
                className="group relative w-full flex justify-center py-2 px-4 border border-transparent text-sm font-medium rounded-md text-white bg-amber-600 hover:bg-amber-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-amber-500 transition duration-150 disabled:opacity-70 disabled:cursor-not-allowed"
              >
                <span className="absolute left-0 inset-y-0 flex items-center pl-3">
                  {isLoading ? (
                    <Loader className="h-5 w-5 text-amber-500 animate-spin" />
                  ) : (
                    <LogIn className="h-5 w-5 text-amber-500 group-hover:text-amber-400" />
                  )}
                </span>
                {isLoading ? "Выполняется вход..." : "Войти"}
              </button>
            </div>
          </div>
        ) : (
          // Форма регистрации
          <div className="mt-8 space-y-6">
            <div className="space-y-4">
              <div>
                <label htmlFor="reg-email" className="block text-sm font-medium text-gray-700">
                  Электронная почта
                </label>
                <div className="mt-1 relative rounded-md shadow-sm">
                  <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                    <Mail className="h-5 w-5 text-amber-500" />
                  </div>
                  <input
                    id="reg-email"
                    name="email"
                    type="email"
                    required
                    value={registerForm.email}
                    onChange={handleRegisterChange}
                    className="block w-full pl-10 pr-3 py-2 border border-amber-300 rounded-md focus:outline-none focus:ring-amber-500 focus:border-amber-500"
                    placeholder="example@mail.com"
                    disabled={isLoading}
                  />
                </div>
              </div>

              <div>
                <label htmlFor="login" className="block text-sm font-medium text-gray-700">
                  Логин
                </label>
                <div className="mt-1 relative rounded-md shadow-sm">
                  <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                    <User className="h-5 w-5 text-amber-500" />
                  </div>
                  <input
                    id="login"
                    name="login"
                    type="text"
                    required
                    value={registerForm.login}
                    onChange={handleRegisterChange}
                    className="block w-full pl-10 pr-3 py-2 border border-amber-300 rounded-md focus:outline-none focus:ring-amber-500 focus:border-amber-500"
                    placeholder="username"
                    disabled={isLoading}
                  />
                </div>
              </div>

              <div>
                <label htmlFor="reg-password" className="block text-sm font-medium text-gray-700">
                  Пароль
                </label>
                <div className="mt-1 relative rounded-md shadow-sm">
                  <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                    <Lock className="h-5 w-5 text-amber-500" />
                  </div>
                  <input
                    id="reg-password"
                    name="password"
                    type="password"
                    required
                    value={registerForm.password}
                    onChange={handleRegisterChange}
                    className="block w-full pl-10 pr-3 py-2 border border-amber-300 rounded-md focus:outline-none focus:ring-amber-500 focus:border-amber-500"
                    placeholder="••••••••"
                    disabled={isLoading}
                  />
                </div>
              </div>

              <div>
                <label htmlFor="confirmPassword" className="block text-sm font-medium text-gray-700">
                  Подтверждение пароля
                </label>
                <div className="mt-1 relative rounded-md shadow-sm">
                  <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                    <Key className="h-5 w-5 text-amber-500" />
                  </div>
                  <input
                    id="confirmPassword"
                    name="confirmPassword"
                    type="password"
                    required
                    value={registerForm.confirmPassword}
                    onChange={handleRegisterChange}
                    className="block w-full pl-10 pr-3 py-2 border border-amber-300 rounded-md focus:outline-none focus:ring-amber-500 focus:border-amber-500"
                    placeholder="••••••••"
                    disabled={isLoading}
                  />
                </div>
              </div>
            </div>

            <div>
              <button
                onClick={handleRegisterSubmit}
                disabled={isLoading}
                className="group relative w-full flex justify-center py-2 px-4 border border-transparent text-sm font-medium rounded-md text-white bg-amber-600 hover:bg-amber-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-amber-500 transition duration-150 disabled:opacity-70 disabled:cursor-not-allowed"
              >
                <span className="absolute left-0 inset-y-0 flex items-center pl-3">
                  {isLoading ? (
                    <Loader className="h-5 w-5 text-amber-500 animate-spin" />
                  ) : (
                    <User className="h-5 w-5 text-amber-500 group-hover:text-amber-400" />
                  )}
                </span>
                {isLoading ? "Регистрация..." : "Зарегистрироваться"}
              </button>
            </div>
          </div>
        )}

        <div className="text-center mt-4">
          <button
            onClick={toggleForm}
            disabled={isLoading}
            className="text-amber-600 hover:text-amber-800 text-sm font-medium transition duration-150 disabled:opacity-70 disabled:cursor-not-allowed"
          >
            {isLogin ? "Нет аккаунта? Зарегистрируйтесь" : "Уже есть аккаунт? Войдите"}
          </button>
        </div>
      </div>
    </div>
  );
};

export default AuthPage;