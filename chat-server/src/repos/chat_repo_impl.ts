import mongoose from "mongoose";
import { ObjectId } from "bson";

import Chat from "../data/chat";
import ChatRepo from "./chat_repo";
import ChatModel, { IChat } from "../db/models";
import IllegalInputError from "../errors/illegal_input_error";

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
        try {
            let savedChat = await newChat.save();
            return this.convertIChatToChat(savedChat);
        } catch (e) {
            if (e.code === 11000) {
                throw new IllegalInputError("Chat already exists");
            }
            throw e;
        }
    }    

    async getFromTo(fromId?: Object, toId?: Object): Promise<Chat[]> {
        let query = {};
        if (fromId && toId) {
            query = {
                ...query,
                "sender.id": fromId,
                "receipient.data.id": toId  
            };
        } else if (fromId) {
            query = {
                ...query,
                "sender.id": fromId    
            };
        } else if (toId) {
            query = { ...query,  "receipient.data.id": toId }
        }
        let results = await this.model.find(query)
        return results.map(this.convertIChatToChat);
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