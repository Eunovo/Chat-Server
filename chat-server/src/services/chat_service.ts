import Chat from '../data/chat';

export default interface ChatService {

    addChat(chat: Chat): Promise<Chat>;
    getChats(): Promise<Chat[]>;
    getChatsFrom(id: Object): Promise<Chat[]>;
    getChatsTo(id: Object): Promise<Chat[]>;
    getChatsFromTo(fromId: Object, toId: Object): Promise<Chat[]>;
}