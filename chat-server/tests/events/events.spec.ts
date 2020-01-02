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
        });

        xit('should reject non authenticated client', () => { });
    });

    describe('NEW_CHAT test', () => {
        it('should send new chat event to receipient and sender',
            (done) => {
                const senderClient = client(serverUrl);
                const receipientClient = client(serverUrl);
                const token = { token: validToken };
                const data = {
                    message: "Hello World!",
                    timestamp: new Date()
                };

                const holdDone = holdFunction(done, 2);

                senderClient.emit('AUTHENTICATE', token);
                receipientClient.emit('AUTHENTICATE', token);
                receipientClient.on('AUTHENTICATED', (response) => {
                    senderClient.emit('NEW_CHAT', {
                        receipient: response, ...data
                    });
                    senderClient.on('NEW_CHAT', (response) => {
                        expect(response).
                            to.equal(data.timestamp.toISOString());
                        senderClient.close();
                        holdDone();
                    });

                    receipientClient.on('NEW_CHAT', (response) => {
                        expect(response.message.data)
                            .to.equal(data.message);
                        receipientClient.close();
                        holdDone();
                    });
                });
            });
    });

});

function holdFunction(callback: Function, numberOfTimes: number) {
    let numberOfTimesCalled = 0;
    return () => {
        numberOfTimesCalled += 1;
        if (numberOfTimesCalled === numberOfTimes) {
            callback();
        }
    }
}