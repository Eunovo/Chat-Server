import Chat from '../data/chat';

export default interface ChatService {

    addChat(chat: Chat): Promise<Chat>;
    getChats(fromTime?: Date): Promise<Chat[]>;
    getChatsFrom(id: Object, fromTime?: Date): Promise<Chat[]>;
    getChatsTo(id: Object, fromTime?: Date): Promise<Chat[]>;
    getChatsFromTo(fromId: Object, toId: Object, fromTime?: Date): Promise<Chat[]>;
}