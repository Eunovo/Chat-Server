import crypto from "crypto";
import mongoose from "mongoose";

import { UserInfo, Receipient, Message } from "../data/chat";

export interface IChat extends mongoose.Document {
    sender: UserInfo;
    receipient: Receipient;
    message: Message;
    hash: string;
};


const UserInfoSchema = new mongoose.Schema({
    id: String,
    username: String,
});

export const ChatSchema = new mongoose.Schema({
    sender: UserInfoSchema,
    receipient: {
        type: { type: String },
        data: { type: UserInfoSchema }
    },
    message: {
        type: String,
        data: String
    },
    hash: { type: String, unique: true }
});

ChatSchema.pre("save", function (next) {
    console.log(this);
    let hash = JSON.stringify(this);
    hash = crypto.createHash('sha1').update(hash).digest('hex');
    (this as IChat).hash = hash;
    next();
});

const Chat = mongoose.model<IChat>('Chat', ChatSchema);
export default Chat;
