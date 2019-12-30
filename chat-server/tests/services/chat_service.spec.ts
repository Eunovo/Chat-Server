import chai from "chai";
import chaiAsPromised from "chai-as-promised";
import mocha from "mocha";
import mock from "mock-require";

import Chat, { UserInfo, Receipient, Message } from "../../src/data/chat";
import ChatService from "../../src/services/chat_service";
import ConstraintViolationError from "../../src/errors/contraint_violation_error";

import { dummyId, sender, receipient, message, dummyChat } from '../fixtures';

chai.use(chaiAsPromised);
const expect = chai.expect;
const should = chai.should();

mock("../../src/repos", "../fixtures");

import { chatService } from "../../src/services";

describe("Chat Service Tests", () => {
    it("should add a chat", async () => {
        const savedChat = await chatService.addChat(new Chat(
            null, sender, receipient, message, new Date()
        ));
        expect(savedChat.id.toString())
            .to.equal(dummyId.toHexString());
    });

    it("should not add an invalid chat", () => {
        const saveChatOperation = async () => await chatService
            .addChat(new Chat(
                null, null, receipient, message, new Date()
            ));
        saveChatOperation().should
            .be.rejectedWith(ConstraintViolationError);
    });

    it("should return chats with a given sender", async () => {
        const senderId = sender.id;
        const expResult = [dummyChat];
        const result = await chatService.getChatsFrom(senderId);
        expect(result[0]).to.equal(expResult[0]);
    });

    it("should return chats with to a given recipient", async () => {
        const recpId = receipient.data.id;
        const expResult = [dummyChat];
        const result = await chatService.getChatsTo(recpId);
        expect(result[0]).to.equal(expResult[0]);
    });

    it("should get chats from a sender to a receipient", async () => {
        const senderId = sender.id;
        const recpId = receipient.data.id;
        const expResult = [dummyChat];
        const result = await chatService.getChatsFromTo(senderId, recpId);
        expect(result[0]).to.equal(expResult[0]);
    });
});