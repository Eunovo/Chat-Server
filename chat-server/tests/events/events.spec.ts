import client from "socket.io-client";
import mocha from "mocha";
import chai from "chai";
import mock from "mock-require";

import { validToken, invalidToken } from "../fixtures";

mock("../../src/services", "../fixtures");

import server, { port } from "../../src/app";

const expect = chai.expect;
const serverUrl = `http://localhost:${port}`;

describe('WebSocket Event test', () => {
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
    });

    describe('AUTHENTICATE test', () => {
        it('should authenticate the client', (done) => {
            const wsClient = client(serverUrl);
            const data = { token: validToken };
    
            wsClient.on('connection', () => console.log('Client connected'));
            wsClient.emit('AUTHENTICATE', data);
            wsClient.on('AUTHENTICATED', (response) => {
                wsClient.close();
                done();
            });
        });

        it('should not authenticate the client', (done) => {
            const wsClient = client(serverUrl);
            const data = { token: invalidToken };
            const expectedMessage = "Invalid Token";
    
            wsClient.on('connection', () => console.log('Client connected'));
            wsClient.emit('AUTHENTICATE', data);
            wsClient.on('AUTHENTICATION_FAILED', (response) => {
                expect(response).to.equal(expectedMessage);
                wsClient.close();
                done();
            });
            wsClient.on('AUTHENTICATED', () => {
                wsClient.close();
                expect.fail("Client should not be authenticated");
            });
        })
    });

});