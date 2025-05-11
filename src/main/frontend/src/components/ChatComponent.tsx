import React, { useState, useEffect, useRef } from 'react';
import { Send, ArrowLeft, Download, FileText, AlertCircle, Loader } from 'lucide-react';
import apiService from '../services/apiService';

// Типы сообщений
interface Message {
  id: string;
  content: string;
  sender: 'user' | 'ai';
  timestamp: Date;
  error?: boolean; // Флаг для сообщений, которые не удалось отправить
}

// Тип для чата
export interface ChatData {
  id: string;
  title: string;
}

// Пример опций для договора
interface ContractOption {
  id: string;
  title: string;
}

// Интерфейс пропсов компонента
export interface ChatComponentProps {
  chat: ChatData | null;
  onBack: () => void;
}

const ChatComponent: React.FC<ChatComponentProps> = ({ chat, onBack }) => {
  // Состояния для сообщений и ввода
  const [messages, setMessages] = useState<Message[]>([]);
  const [inputMessage, setInputMessage] = useState('');
  const [title, setTitle] = useState('Новый договор аренды');
  const [isGenerating, setIsGenerating] = useState(false);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [downloadProgress, setDownloadProgress] = useState<number | null>(null);

  // Опции для создания разных типов договоров
  const contractOptions: ContractOption[] = [
    { id: 'apartment', title: 'Квартира' },
    { id: 'house', title: 'Частный дом' },
    { id: 'commercial', title: 'Коммерческое помещение' },
    { id: 'office', title: 'Офис' },
    { id: 'land', title: 'Земельный участок' }
  ];

  // Ссылка на контейнер сообщений для автоскролла
  const messagesEndRef = useRef<null | HTMLDivElement>(null);

  // Функция автоскролла в конец списка сообщений
  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  };

  // Установка заголовка чата при изменении пропса chat
  useEffect(() => {
    if (chat && chat.title) {
      setTitle(chat.title);
    }
  }, [chat]);

  // Загрузка сообщений при изменении ID чата
  useEffect(() => {
    if (chat && chat.id) {
      const fetchMessages = async () => {
        try {
          setIsLoading(true);
          setError(null);

          // Загружаем сообщения через API
          const messagesData = await apiService.messages.getMessages(chat.id);

          // Преобразуем даты из строк в объекты Date
          const formattedMessages = messagesData.map(msg => ({
            ...msg,
            timestamp: new Date(msg.timestamp)
          }));

          setMessages(formattedMessages);

          // Если сообщений нет, добавляем приветственное сообщение от AI
          if (formattedMessages.length === 0) {
            setTimeout(() => {
              setMessages([{
                id: '1',
                content: 'Здравствуйте! Я AI-помощник по составлению договоров аренды. Какой тип помещения вы хотите сдать в аренду?',
                sender: 'ai',
                timestamp: new Date()
              }]);
            }, 1000);
          }
        } catch (error) {
          console.error('Failed to fetch messages:', error);
          setError('Не удалось загрузить сообщения. Пожалуйста, попробуйте позже.');

          // Если произошла ошибка, добавляем стандартное приветствие
          setTimeout(() => {
            setMessages([{
              id: '1',
              content: 'Здравствуйте! Я AI-помощник по составлению договоров аренды. Какой тип помещения вы хотите сдать в аренду?',
              sender: 'ai',
              timestamp: new Date()
            }]);
          }, 1000);
        } finally {
          setIsLoading(false);
        }
      };

      fetchMessages();
    }
  }, [chat]);

  // При добавлении новых сообщений скроллим в конец
  useEffect(() => {
    scrollToBottom();
  }, [messages]);

  // Обработчик отправки сообщения
  const handleSendMessage = async () => {
    if (inputMessage.trim() === '' || !chat || isGenerating) return;

    // Временное ID для сообщения пользователя
    const tempId = `temp-${Date.now()}`;

    // Создаем сообщение пользователя
    const userMessage: Message = {
      id: tempId,
      content: inputMessage,
      sender: 'user',
      timestamp: new Date()
    };

    // Добавляем сообщение в список и очищаем поле ввода
    setMessages(prev => [...prev, userMessage]);
    setInputMessage('');

    try {
      setIsGenerating(true);
      setError(null);

      // Отправляем сообщение через API
      const sentMessage = await apiService.messages.sendMessage(chat.id, inputMessage);

      // Заменяем временное сообщение на полученное от сервера
      setMessages(prev =>
        prev.map(msg =>
          msg.id === tempId
            ? { ...sentMessage, timestamp: new Date(sentMessage.timestamp) }
            : msg
        )
      );

      // Ожидаем ответ от AI
      // Здесь можно добавить логику ожидания ответа от сервера
      // или использовать WebSocket для получения ответа

      // Имитация получения ответа от AI (в реальном приложении здесь будет API-вызов)
      setTimeout(async () => {
        try {
          // Получаем новые сообщения с сервера после нашего сообщения
          const newMessages = await apiService.messages.getMessages(chat.id);

          // Находим ответ AI (последнее сообщение от AI)
          const aiResponses = newMessages
            .filter(msg => msg.sender === 'ai' && new Date(msg.timestamp) > userMessage.timestamp)
            .map(msg => ({
              ...msg,
              timestamp: new Date(msg.timestamp)
            }));

          // Добавляем ответы AI в список сообщений
          if (aiResponses.length > 0) {
            setMessages(prev => [...prev, ...aiResponses]);
          } else {
            // Если ответа от AI нет, имитируем ответ
            const aiMessage: Message = {
              id: `ai-${Date.now()}`,
              content: getAIResponse(inputMessage),
              sender: 'ai',
              timestamp: new Date()
            };
            setMessages(prev => [...prev, aiMessage]);
          }
        } catch (error) {
          console.error('Failed to get AI response:', error);
          setError('Не удалось получить ответ ассистента. Пожалуйста, попробуйте еще раз.');
        } finally {
          setIsGenerating(false);
        }
      }, 1500);
    } catch (error) {
      console.error('Failed to send message:', error);
      setError('Не удалось отправить сообщение. Пожалуйста, попробуйте еще раз.');

      // Отмечаем сообщение как не отправленное
      setMessages(prev =>
        prev.map(msg =>
          msg.id === tempId
            ? { ...msg, error: true }
            : msg
        )
      );

      setIsGenerating(false);
    }
  };

  // Простая имитация ответов AI (используется только если API не вернул ответ)
  const getAIResponse = (userInput: string): string => {
    const input = userInput.toLowerCase();

    if (input.includes('квартир')) {
      return 'Отлично! Для составления договора аренды квартиры мне нужна следующая информация:\n\n1. Адрес квартиры\n2. Площадь помещения\n3. Срок аренды\n4. Ежемесячная арендная плата\n5. Размер залога (если предусмотрен)\n\nМожете предоставить эти данные?';
    } else if (input.includes('офис')) {
      return 'Для составления договора аренды офисного помещения потребуется:\n\n1. Юридический адрес помещения\n2. Площадь офиса\n3. Срок аренды\n4. Ежемесячная арендная плата\n5. Условия использования общих зон\n\nКакие данные у вас уже есть?';
    } else if (input.includes('срок') || input.includes('период')) {
      return 'Стандартный срок договора аренды обычно составляет 11 месяцев или 1 год. Вы можете указать любой удобный для вас срок. Какой период аренды вы предпочитаете?';
    } else if (input.includes('оплат') || input.includes('деньги') || input.includes('стоимость')) {
      return 'Арендная плата должна быть четко прописана в договоре. Укажите сумму и валюту платежа, а также сроки оплаты (например, до 5 числа каждого месяца). Как вы планируете организовать оплату?';
    } else {
      return 'Я понял ваш запрос. Чтобы продолжить составление договора, пожалуйста, предоставьте больше деталей о помещении, сроках аренды и условиях оплаты.';
    }
  };

  // Повторная отправка сообщения, которое не удалось отправить
  const handleRetry = async (messageId: string) => {
    // Находим сообщение для повторной отправки
    const messageToRetry = messages.find(msg => msg.id === messageId);
    if (!messageToRetry || !chat) return;

    // Снимаем флаг ошибки
    setMessages(prev =>
      prev.map(msg =>
        msg.id === messageId
          ? { ...msg, error: false }
          : msg
      )
    );

    try {
      setIsGenerating(true);

      // Отправляем сообщение через API
      const sentMessage = await apiService.messages.sendMessage(chat.id, messageToRetry.content);

      // Заменяем сообщение с ошибкой на успешно отправленное
      setMessages(prev =>
        prev.map(msg =>
          msg.id === messageId
            ? { ...sentMessage, timestamp: new Date(sentMessage.timestamp) }
            : msg
        )
      );

      // Получаем ответ от AI (аналогично handleSendMessage)
      setTimeout(async () => {
        try {
          // Логика получения ответа от AI, аналогичная handleSendMessage
          // ...

          const aiMessage: Message = {
            id: `ai-${Date.now()}`,
            content: getAIResponse(messageToRetry.content),
            sender: 'ai',
            timestamp: new Date()
          };

          setMessages(prev => [...prev, aiMessage]);
        } catch (error) {
          console.error('Failed to get AI response:', error);
          setError('Не удалось получить ответ ассистента. Пожалуйста, попробуйте еще раз.');
        } finally {
          setIsGenerating(false);
        }
      }, 1500);
    } catch (error) {
      console.error('Failed to retry sending message:', error);
      setError('Не удалось отправить сообщение. Пожалуйста, попробуйте еще раз.');

      // Возвращаем флаг ошибки
      setMessages(prev =>
        prev.map(msg =>
          msg.id === messageId
            ? { ...msg, error: true }
            : msg
        )
      );

      setIsGenerating(false);
    }
  };

  // Выбор типа договора
  const selectContractType = async (type: ContractOption) => {
    if (!chat) return;

    // Обновляем заголовок
    const newTitle = `Договор аренды: ${type.title}`;
    setTitle(newTitle);

    // Создаем сообщение пользователя
    const userMessage: Message = {
      id: Date.now().toString(),
      content: `Хочу составить договор аренды: ${type.title}`,
      sender: 'user',
      timestamp: new Date()
    };

    setMessages(prev => [...prev, userMessage]);

    try {
      setIsGenerating(true);
      setError(null);

      // Отправляем выбор типа договора через API
      await apiService.messages.sendMessage(chat.id, userMessage.content);

      // Обновляем заголовок чата на сервере
      await apiService.chats.updateChat(chat.id, newTitle);

      // Имитация ответа AI
      setTimeout(() => {
        const aiResponse = type.id === 'apartment'
          ? 'Отлично! Для составления договора аренды квартиры мне нужна следующая информация:\n\n1. Адрес квартиры\n2. Площадь помещения\n3. Срок аренды\n4. Ежемесячная арендная плата\n5. Размер залога (если предусмотрен)\n\nМожете предоставить эти данные?'
          : `Для составления договора аренды "${type.title}" мне потребуется информация о местоположении, площади, сроках и условиях. Какими данными вы располагаете?`;

        const aiMessage: Message = {
          id: (Date.now() + 1).toString(),
          content: aiResponse,
          sender: 'ai',
          timestamp: new Date()
        };

        setMessages(prev => [...prev, aiMessage]);
        setIsGenerating(false);
      }, 1500);
    } catch (error) {
      console.error('Failed to select contract type:', error);
      setError('Не удалось выбрать тип договора. Пожалуйста, попробуйте еще раз.');
      setIsGenerating(false);
    }
  };

  // Обработчик нажатия Enter для отправки сообщения
  const handleKeyPress = (e: React.KeyboardEvent) => {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault();
      handleSendMessage();
    }
  };

  // Скачивание договора
  const handleDownloadContract = async () => {
    if (!chat) return;

    try {
      setDownloadProgress(0);
      setError(null);

      // Создаем обработчик прогресса
      const onProgress = (progress: number) => {
        setDownloadProgress(progress);
      };

      // Скачиваем документ через API
      const documentBlob = await apiService.documents.getDocument(chat.id, onProgress);

      // Создаем ссылку для скачивания
      const url = URL.createObjectURL(documentBlob);
      const a = document.createElement('a');
      a.href = url;
      a.download = `${title.replace(/[^\w\s]/gi, '')}.docx`; // Формируем имя файла из заголовка
      document.body.appendChild(a);
      a.click();

      // Очищаем ссылку
      setTimeout(() => {
        document.body.removeChild(a);
        URL.revokeObjectURL(url);
        setDownloadProgress(null);
      }, 100);
    } catch (error) {
      console.error('Failed to download contract:', error);
      setError('Не удалось скачать договор. Возможно, он еще не сформирован полностью.');
      setDownloadProgress(null);
    }
  };

  return (
    <div className="flex flex-col h-screen bg-amber-50">
      {/* Шапка чата */}
      <header className="bg-white shadow-sm p-4 flex justify-between items-center">
        <div className="flex items-center space-x-3">
          <button
            onClick={onBack}
            className="p-2 rounded-full hover:bg-amber-100 transition duration-150"
          >
            <ArrowLeft className="h-5 w-5 text-amber-800" />
          </button>
          <h1 className="font-medium text-lg text-gray-900">{title}</h1>
        </div>

        <button
          onClick={handleDownloadContract}
          disabled={downloadProgress !== null}
          className="flex items-center space-x-2 px-4 py-2 rounded-lg bg-amber-100 text-amber-800 hover:bg-amber-200 transition duration-150 disabled:opacity-70 disabled:cursor-not-allowed"
        >
          {downloadProgress !== null ? (
            <>
              <Loader className="h-5 w-5 animate-spin" />
              <span>Загрузка... {downloadProgress}%</span>
            </>
          ) : (
            <>
              <Download className="h-5 w-5" />
              <span>Скачать договор</span>
            </>
          )}
        </button>
      </header>

      {/* Сообщение об ошибке */}
      {error && (
        <div className="bg-red-100 border-l-4 border-red-500 text-red-700 p-4 m-4 rounded" role="alert">
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

      {/* Контейнер с сообщениями */}
      <div className="flex-1 overflow-y-auto p-4 space-y-4">
        {isLoading ? (
          <div className="flex justify-center items-center h-full">
            <Loader className="h-8 w-8 text-amber-600 animate-spin" />
            <span className="ml-2 text-amber-600 font-medium">Загрузка сообщений...</span>
          </div>
        ) : messages.length === 0 ? (
          <div className="h-full flex flex-col items-center justify-center text-center">
            <FileText className="h-16 w-16 text-amber-300 mb-4" />
            <h3 className="text-xl font-medium text-gray-800">Создание нового договора</h3>
            <p className="text-gray-500 mt-2 max-w-md">
              Выберите тип помещения, и я помогу вам составить юридически корректный договор аренды
            </p>

            <div className="mt-8 grid grid-cols-2 gap-3 sm:grid-cols-3">
              {contractOptions.map(option => (
                <button
                  key={option.id}
                  onClick={() => selectContractType(option)}
                  className="px-4 py-3 bg-white rounded-lg shadow-sm hover:shadow transition duration-150 text-gray-800"
                >
                  {option.title}
                </button>
              ))}
            </div>
          </div>
        ) : (
          // Список сообщений
          messages.map(message => (
            <div
              key={message.id}
              className={`flex ${message.sender === 'user' ? 'justify-end' : 'justify-start'}`}
            >
              <div
                className={`max-w-xs md:max-w-md lg:max-w-lg xl:max-w-xl rounded-lg px-4 py-2 ${
                  message.sender === 'user'
                    ? message.error
                      ? 'bg-red-200 text-red-800 rounded-br-none'
                      : 'bg-amber-600 text-white rounded-br-none'
                    : 'bg-white text-gray-800 rounded-bl-none'
                }`}
              >
                <div className="whitespace-pre-line">{message.content}</div>
                <div
                  className={`flex justify-between items-center text-xs mt-1 ${
                    message.sender === 'user'
                      ? message.error
                        ? 'text-red-600'
                        : 'text-amber-200'
                      : 'text-gray-400'
                  }`}
                >
                  <span>{message.timestamp.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}</span>

                  {/* Кнопка повторной отправки для сообщений с ошибкой */}
                  {message.error && (
                    <button
                      onClick={() => handleRetry(message.id)}
                      className="ml-2 text-red-600 hover:text-red-800 transition-colors"
                    >
                      Повторить
                    </button>
                  )}
                </div>
              </div>
            </div>
          ))
        )}

        {/* Индикатор набора текста */}
        {isGenerating && (
          <div className="flex justify-start">
            <div className="bg-white rounded-lg px-4 py-2 text-gray-800 rounded-bl-none">
              <div className="flex space-x-1">
                <div className="w-2 h-2 rounded-full bg-gray-400 animate-bounce" style={{ animationDelay: '0ms' }}></div>
                <div className="w-2 h-2 rounded-full bg-gray-400 animate-bounce" style={{ animationDelay: '150ms' }}></div>
                <div className="w-2 h-2 rounded-full bg-gray-400 animate-bounce" style={{ animationDelay: '300ms' }}></div>
              </div>
            </div>
          </div>
        )}

        {/* Элемент для автоскролла */}
        <div ref={messagesEndRef} />
      </div>

      {/* Форма ввода */}
      <div className="bg-white border-t border-gray-200 p-4">
        <div className="flex items-center space-x-2 max-w-4xl mx-auto">
          <div className="flex-1 relative">
            <textarea
              value={inputMessage}
              onChange={(e) => setInputMessage(e.target.value)}
              onKeyPress={handleKeyPress}
              placeholder="Введите сообщение..."
              className="w-full border border-gray-300 rounded-lg py-2 px-4 focus:outline-none focus:ring-2 focus:ring-amber-500 resize-none"
              rows={1}
              style={{ minHeight: '44px', maxHeight: '120px' }}
              disabled={isGenerating || isLoading}
            />
          </div>

          <button
            onClick={handleSendMessage}
            disabled={inputMessage.trim() === '' || isGenerating || isLoading}
            className={`p-2 rounded-full ${
              inputMessage.trim() === '' || isGenerating || isLoading
                ? 'bg-gray-100 text-gray-400'
                : 'bg-amber-600 text-white hover:bg-amber-700'
            } transition duration-150`}
          >
            {isGenerating ? (
              <Loader className="h-5 w-5 animate-spin" />
            ) : (
              <Send className="h-5 w-5" />
            )}
          </button>
        </div>
      </div>
    </div>
  );
};

export default ChatComponent;