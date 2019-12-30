import { ObjectId } from "bson";

import Chat, { UserInfo, Receipient, Message } from "../src/data/chat";
import ChatRepo from "../src/repos/chat_repo";

export const date = new Date();
export const dummyId = new ObjectId(date.getTime() / 1000);
export const sender = new UserInfo("55", "Novo");
export const receipient = new Receipient("single", 
    new UserInfo("56", "Bob"));
export const message = new Message("text", "Hello");
export const dummyChat = new Chat(dummyId, sender, receipient, message, date);

export const chatRepo: ChatRepo = {
    save: (chat: Chat) => Promise.resolve(dummyChat),
    getFromTo: (fromId?: Object, toId?: Object) => {
        if (fromId && toId) {
            if (fromId == sender.id && toId == receipient.data.id) {
                return Promise.resolve([dummyChat]);
            }
            return Promise.resolve([]);
        }
        if (fromId && fromId == sender.id) {
            return Promise.resolve([dummyChat]);
        }
        if (toId && toId == receipient.data.id) {
            return Promise.resolve([dummyChat]);
        }
        return Promise.resolve([]);
    },
}