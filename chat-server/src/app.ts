import dotenv from "dotenv";
import express from "express";
import http from "http";
import socketio, { Socket } from "socket.io";
import redis from "socket.io-redis";

import ApiResponse from './models/response';
import controller from './controllers/event_controller';
import router from './routes';
import { authMiddleware, errorHandler } from "./helpers/express-utils";
import { logRequest, logError } from "./helpers/logger";

dotenv.config();

const app = express();
app.use(logRequest);
app.use(express.json());
app.use(authMiddleware);
router(app);
app.use(logError);
app.use(errorHandler);
app.use((err, req, res, next) => {
    res.status(500)
        .json(ApiResponse.error("An error occurred", []));
});

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
    controller(io, client);
});

const server = httpServer.listen(port, function () {
    console.log(`Listening on *:${port}`);
});
server.on("close", () => {
    console.log("Server closed");
});

export default server;