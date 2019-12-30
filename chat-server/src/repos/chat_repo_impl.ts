import mongoose from "mongoose";
import { ObjectId } from "bson";

import Chat from "../data/chat";
import ChatRepo from "./chat_repo";
import ChatModel, { IChat } from "../db/models";

export default class MongooseChatRepo implements ChatRepo {
    model: mongoose.Model<IChat>;
    constructor() {
        this.model = ChatModel;
    }

    async save(chat: Chat): Promise<Chat> {
        let newChat = new ChatModel({
            sender: chat.sender,
            receipient: chat.receipient,
            message: chat.message,
            timestamp: chat.timestamp,
        });
        let savedChat = await newChat.save();
        return this.convertIChatToChat(savedChat);
    }    

    getFromTo(fromId?: Object, toId?: Object): Promise<Chat[]> {
        throw new Error("Method not implemented.");
    }

    convertIChatToChat(ichat: IChat): Chat {
        let { _id, sender, receipient, message, timestamp } = ichat;
        let createdAt = new ObjectId(_id).getTimestamp();
        let chat = new Chat(_id, sender, receipient, 
            message, timestamp);
        chat.createdAt = createdAt;
        return chat;
    }

}