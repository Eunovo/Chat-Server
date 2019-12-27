import chai from "chai";
import mocha from "mocha";

import Chat, { UserInfo, Receipient, Message } from "../../src/data/chat";
import ConstraintViolationError from '../../src/errors/contraint_violation_error';

const expect = chai.expect;

describe("Chat Data test", () => {

    it("should validate UserInfo", () => {
        let validId = "testId";
        let validUsername = "Novo";
        let createWithId = (id: any) =>
            () => (new UserInfo(id, validUsername)).validate();
        let createWithUsername = (username: any) =>
            () => (new UserInfo(validId, username)).validate();

        expect(createWithId(null)).to.throw(ConstraintViolationError);
        expect(createWithUsername("")).to.throw(ConstraintViolationError);
    });

    it("should validate Receipient", () => {
        let validType: "single" = "single";
        let invalidData = new UserInfo("", "");
        let createWithData = (data: any) =>
            () => (new Receipient(validType, data)).validate();

        expect(createWithData(null)).to.throw(ConstraintViolationError);
        expect(createWithData(invalidData)).to.throw(ConstraintViolationError);
    });

    it("should validate chat", () => {
        let invalidSender = new UserInfo("", "");
        let validSender = new UserInfo("id", "novo");
        let validReceipient = new Receipient("single",
            new UserInfo("id", "novo"));
        let validMessage = new Message("text", "hello");
        let createWithId = (id) => () => new Chat(id, validSender, 
            validReceipient, validMessage, 
            new Date()).validate();
        let createWithSender = (sender) =>
            () => (new Chat("id", sender, validReceipient,
                validMessage, new Date())).validate();

        expect(createWithId("")).to.throw(ConstraintViolationError);
        expect(createWithSender(invalidSender))
            .to.throw(ConstraintViolationError);
    });
});