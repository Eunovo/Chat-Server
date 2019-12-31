import { Socket } from "socket.io";

import { authService } from "./services";

export default function (client: Socket) {
    client.on("PING", (data: string) => {
        client.emit("PONG", data);
    });

    client.on("AUTHENTICATE", async (data: any) => {
        let { token } = data;
        try {
            let userInfo = await authService.authenticate(token);
            client.emit("AUTHENTICATED");
        } catch (e) {
            client.emit("AUTHENTICATION_FAILED", e.message);
        }
    });
}