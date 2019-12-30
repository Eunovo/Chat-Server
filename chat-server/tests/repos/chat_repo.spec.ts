import chai from "chai";
import chaiAsPromised from "chai-as-promised";
import mocha from "mocha";

import Chat from "../../src/data/chat";
import connectDb, { disconnect } from "../../src/db/config";
import ChatModel from "../../src/db/models";
import IllegalInputError from "../../src/errors/illegal_input_error";

import { chatRepo } from "../../src/repos";
import { sender, receipient, message, date } from "../fixtures";

chai.use(chaiAsPromised);
const expect = chai.expect;
const should = chai.should();

describe("Repo Test", () => {
    before(() => {
        connectDb("mongodb://localhost:27017/ChatServerTest");
    });

    beforeEach((done) => {
        ChatModel.deleteMany({}, () => done());
    });

    after(async () => {
        await disconnect();
    });

    it("should save a chat", async () => {
        let savedChat = await chatRepo.save(new Chat(
            null, sender, receipient, message, date
        ));
        expect(savedChat.sender.id).to.equal(sender.id);
        expect(savedChat.id).to.not.equal(undefined);
    });

    it("should reject duplicate chats", () => {
        let operation = async () => {
            await chatRepo.save(new Chat(
                null, sender, receipient, message, date
            ));
            await chatRepo.save(new Chat(
                null, sender, receipient, message, date
            ));
        }
        operation().should.be.rejectedWith(IllegalInputError);
    });

    xit("should get chats from", () => {});
    xit("should get chats to", () => {});
    xit("should get chats from and to", () => {});
});