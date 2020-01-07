import Chat from '../data/chat';

import ChatService from './chat_service';
import { chatRepo } from '../repos';

export default class ChatServiceImpl implements ChatService {
    addChat(chat: Chat): Promise<Chat> {
        chat.validate();
        return chatRepo.save(chat);
    }    
    getChats(): Promise<Chat[]> {
        throw new Error("Method not implemented.");
    }
    getChatsFrom(id: Object): Promise<Chat[]> {
        return chatRepo.getFromTo(id);
    }
    getChatsTo(id: Object, fromTime?: Date): Promise<Chat[]> {
        return chatRepo.getFromTo(undefined, id, fromTime);
    }
    getChatsFromTo(fromId: Object, toId: Object): Promise<Chat[]> {
        return chatRepo.getFromTo(fromId, toId);
    }
}