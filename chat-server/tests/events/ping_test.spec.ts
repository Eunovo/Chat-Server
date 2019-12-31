import client from "socket.io-client";
import mocha from "mocha";
import chai from "chai";

import server, { port } from "../../src/app";

const expect = chai.expect;
const serverUrl = `http://localhost:${port}`;

describe('WebSocket ping test', () => {
    after((done) => {
        server.close((err) => {
            if (err) {
                console.log(err);
            }
            done();
        });
    });

    it('PING test', (done) => {
        const wsClient = client(serverUrl);
        const data = "test";

        wsClient.on('connection', () => console.log('Client connected'));
        wsClient.emit('PING', data);
        wsClient.on('PONG', (response) => {
            expect(response).to.equal(data);
            wsClient.close();
            done();
        });
    })
});