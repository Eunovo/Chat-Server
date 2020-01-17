import client from "socket.io-client";
import mocha from "mocha";
import mock from "mock-require";

import testAPI from "./api.spec";
import testWS from "./event.spec";

mock("../../src/services", "../fixtures");

import server, { port } from "../../src/app";

const serverUrl = `http://localhost:${port}`;

describe('Endpoints', () => {

    after((done) => {
        server.close((err) => {
            if (err) {
                console.log(err);
            }
            done();
        });
    });

    testAPI(serverUrl);
    testWS(serverUrl);

});