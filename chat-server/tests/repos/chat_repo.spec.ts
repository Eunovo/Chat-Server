import chai from "chai";
import mocha from "mocha";

import Chat from "../../src/data/chat";
import connectDb, { disconnect } from "../../src/db/config";
import ChatModel from "../../src/db/models";
import { chatRepo } from "../../src/repos";
import { sender, receipient, message, date } from "../fixtures";

const expect = chai.expect;

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

    xit("should reject duplicate chats", () => {

    });
});