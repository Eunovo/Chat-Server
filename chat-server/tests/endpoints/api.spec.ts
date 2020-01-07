import chai from "chai";
import chaiHttp from "chai-http";
import HttpStatus from "http-status-codes";
import mocha from "mocha";

chai.use(chaiHttp);
chai.should();

import {
    date, lastSeenChats,
    validToken, invalidToken
} from "../fixtures";

const apiPrefix = "/api/v1";
export default (server: any) => {
    describe("API test", () => {
        describe("Authentication", () => {
            it('should authenticate user', (done) => {
                chai.request(server)
                    .get(apiPrefix + "/test")
                    .set('Authorization', `bearer ${validToken}`)
                    .end((err, res) => {
                        res.should.not.have.status(HttpStatus.FORBIDDEN);
                        done();
                    });
            });

            it('should not authenticate user with invalid token',
                (done) => {
                    chai.request(server)
                        .get(apiPrefix + "/test")
                        .set('Authorization', `bearer ${invalidToken}`)
                        .end((err, res) => {
                            res.should.have.status(HttpStatus.FORBIDDEN);
                            done();
                        });
                });

            it('should not authenticate user without token',
                (done) => {
                    chai.request(server)
                        .get(apiPrefix + "/test")
                        .end((err, res) => {
                            res.should.have.status(HttpStatus.FORBIDDEN);
                            done();
                        });
                });

            it('should not authenticate user without bearer token',
                (done) => {
                    chai.request(server)
                        .get(apiPrefix + "/test")
                        .set('Authorization', validToken)
                        .end((err, res) => {
                            res.should.have.status(HttpStatus.FORBIDDEN);
                            done();
                        });
                });
        });

        describe("Chat API", () => {
            it('should get latest chats', (done) => {
                const lastSeen = date.toUTCString();
                chai.request(server)
                    .get(apiPrefix + `/latest/${lastSeen}`)
                    .set('Authorization', `bearer ${validToken}`)
                    .end((err, res) => {
                        console.log(res.body);
                        res.should.have.status(HttpStatus.OK);
                        res.body.data.should.have
                            .property('length', lastSeenChats.length);
                        done();
                    });
            });
        });
    });
}