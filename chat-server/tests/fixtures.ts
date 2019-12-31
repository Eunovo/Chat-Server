import { ObjectId } from "bson";

import AuthService from "../src/services/auth_service";
import Chat, { UserInfo, Receipient, Message } from "../src/data/chat";
import ChatRepo from "../src/repos/chat_repo";

export const validToken = "valid_token";
export const invalidToken = "invalid_token";

export const authService: AuthService = {
    authenticate: (token: string) => {
        if (token === validToken) {
            return Promise.resolve(new UserInfo("1", "Novo"));
        } else if (token === invalidToken) {
            return Promise.reject(new Error("Invalid Token"));
        }
        return Promise.reject(new Error("Incorrect Token"));
    }
}

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