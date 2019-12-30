import ChatService from './chat_service';
import ChatServiceImpl from './chat_service_impl';

export const chatService: ChatService = new ChatServiceImpl();