import chai from "chai";
import client from "socket.io-client";
import mocha from "mocha";

import { holdFunction } from "../../src/helpers/utils";
import { validToken, invalidToken } from "../fixtures";

const expect = chai.expect;

export default (serverUrl: string) => {
    describe("WebSocket", () => {
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
                    expect(response.message).to.equal(expectedMessage);
                    wsClient.close();
                    done();
                });
                wsClient.on('AUTHENTICATED', () => {
                    wsClient.close();
                    expect.fail("Client should not be authenticated");
                    done();
                });
            });

            it('should reject non authenticated client', (done) => {
                const wsClient = client(serverUrl);

                wsClient.on('connection', () => console.log('Client connected'));
                wsClient.emit('NEW_CHAT');
                wsClient.on('AUTHENTICATION_REQUIRED', (reponse) => {
                    wsClient.close();
                    done();
                })
                wsClient.on('CHAT_SUCCESS', () => {
                    expect.fail("Request should be rejected");
                    wsClient.close();
                    done();
                });
                wsClient.on('CHAT_ERROR', () => {
                    expect.fail("Request should be rejected");
                    wsClient.close();
                    done();
                });
            });
        });

        describe('CHAT Event test', () => {
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
                            receipient: response.data, ...data
                        });
                        senderClient.on('CHAT_SUCCESS', (response) => {
                            expect(response.data).
                                to.equal(data.timestamp.toISOString());
                            senderClient.close();
                            holdDone();
                        });

                        receipientClient.on('NEW_CHAT', (response) => {
                            expect(response.data.message.data)
                                .to.equal(data.message);
                            receipientClient.close();
                            holdDone();
                        });
                    });
                });

        });
    });
}