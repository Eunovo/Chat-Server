import Chat from '../data/chat';

export default interface ChatRepo {

    save(chat: Chat): Promise<Chat>;
    getFromTo(fromId?: Object, toId?: Object): Promise<Chat[]>;
}