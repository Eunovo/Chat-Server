import crypto from "crypto";
import mongoose from "mongoose";

import { UserInfo, Receipient, Message } from "../data/chat";

export interface IChat extends mongoose.Document {
    sender: UserInfo;
    receipient: Receipient;
    message: Message;
    timestamp: Date;
    hash: string;
};


const UserInfoType = {
    id: String,
    username: String,
};

export const ChatSchema = new mongoose.Schema({
    sender: UserInfoType,
    receipient: {
        type: { type: String },
        data: UserInfoType
    },
    message: {
        type: { type: String },
        data: String
    },
    timestamp: { type: Date },
    hash: { type: String, unique: true }
});

ChatSchema.pre("save", function (next) {
    let { sender, receipient, message, timestamp } = this as any;
    let hash = JSON.stringify({ sender, receipient, message, timestamp });
    hash = crypto.createHash('sha1').update(hash).digest('hex');
    (this as IChat).hash = hash;
    next();
});

const Chat = mongoose.model<IChat>('Chat', ChatSchema);
export default Chat;
