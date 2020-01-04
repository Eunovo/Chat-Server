import { Socket, Server } from "socket.io";

import Chat, { Message, UserInfo, Receipient } from "./data/chat";
import Response from "./models/response";
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
            client.emit("AUTHENTICATED", 
                Response.success(currentUserInfo, "Authenticated"));
        } catch (e) {
            client.emit("AUTHENTICATION_FAILED", 
                Response.error(e.message, 500));
        }
    });

    client.on("NEW_CHAT", async (data: any) => {
        let { receipient, message, timestamp } = data;
        let savedChat = await chatService.addChat(new Chat(
            null, currentUserInfo, 
            new Receipient("single", receipient), 
            new Message("text", message), 
            new Date(timestamp)));
        client.emit("NEW_CHAT", 
            Response.success(savedChat.timestamp, "Lastest chat processed"));
        let recpConnId = connectionMap.get(receipient.id);
        io.to(recpConnId).emit("NEW_CHAT", 
            Response.success(savedChat, "New chat"));
    });

    client.on("disconnect", () => {
        // TODO delete connection id from the map
    });
}