import { Socket, Server } from "socket.io";

import Chat, { Message, UserInfo, Receipient } from "./data/chat";
import { authService, chatService } from "./services";

const connectionMap = new Map<any, any>();

export default function (io: Server, client: Socket) {
    let currentUserInfo:UserInfo = null;

    client.on("PING", (data: string) => {
        client.emit("PONG", data);
    });

    client.on("AUTHENTICATE", async (data: any) => {
        let { token } = data;
        try {
            currentUserInfo = await authService.authenticate(token);
            connectionMap.set(currentUserInfo.id, client.id);
            client.emit("AUTHENTICATED", currentUserInfo);
        } catch (e) {
            client.emit("AUTHENTICATION_FAILED", e.message);
        }
    });

    client.on("NEW_CHAT", async (data: any) => {
        let { receipient, message, timestamp } = data;
        let savedChat = await chatService.addChat(new Chat(
            null, currentUserInfo, 
            new Receipient("single", receipient), 
            new Message("text", message), 
            new Date(timestamp)));
        client.emit("NEW_CHAT", savedChat.timestamp);
        let recpConnId = connectionMap.get(receipient.id);
        io.to(recpConnId).emit("NEW_CHAT", savedChat);
    });

    client.on("disconnect", () => {
        // TODO delete connection id from the map
    });
}