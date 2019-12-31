import dotenv from "dotenv";
import express from "express";
import http from "http";
import socketio, { Socket } from "socket.io";
import redis from "socket.io-redis";

import controller from './event_controllers';

dotenv.config();

const app = express();
const redisHost = process.env.REDIS_HOST || "localhost";
const redisPort = parseInt(process.env.REDIS_PORT || '6379');
export const port = process.env.PORT || 3000;

let httpServer = new http.Server(app);
let io = socketio(httpServer);

if (process.env.NODE_ENV === 'production') {
    io.adapter(redis({ host: redisHost, port: redisPort }));;
}

io.on("connection", function (client: Socket) {
    console.log(`User, id ${client.id} connected ` +
        `with address ${client.handshake.address}`);
    controller(client);
});

const server = httpServer.listen(port, function () {
    console.log(`Listening on *:${port}`);
});
server.on("close", () => {
    console.log("Server closed");
});

export default server;