import AuthService, { AuthServiceImpl } from './auth_service';
import ChatService from './chat_service';
import ChatServiceImpl from './chat_service_impl';

export const authService: AuthService = new AuthServiceImpl();
export const chatService: ChatService = new ChatServiceImpl();