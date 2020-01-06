import chai from "chai";
import chaiAsPromised from "chai-as-promised";
import mocha from "mocha";

import Chat, { Receipient, UserInfo } from "../../src/data/chat";
import connectDb, { disconnect } from "../../src/db/config";
import ChatModel from "../../src/db/models";
import IllegalInputError from "../../src/errors/illegal_input_error";

import { chatRepo } from "../../src/repos";
import { sender, receipient, message, date } from "../fixtures";

chai.use(chaiAsPromised);
const expect = chai.expect;
const should = chai.should();

const conversation = [
    {
        sender, receipient, message,
        timestamp: date, hash: "a"
    },
    {
        sender: receipient.data,
        receipient: new Receipient("single", sender),
        message,
        timestamp: new Date(date.getTime() + 1000),
        hash: "b"
    },
    {
        sender, receipient: new Receipient("single",
            new UserInfo("100", "Zee")),
        message,
        timestamp: new Date(date.getTime() + 2000),
        hash: "c"
    },
];

describe("Repo Test", () => {
    before(() => {
        connectDb("mongodb://localhost:27017/ChatServerTest");
    });

    beforeEach(async () => {
        await ChatModel.deleteMany({});
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

    it("should reject duplicate chats", async () => {
        await chatRepo.save(new Chat(
            null, sender, receipient, message, date
        ));
        let savePromise = chatRepo.save(new Chat(
            null, sender, receipient, message, date
        ));
        savePromise.should.be.rejectedWith(IllegalInputError);
    });

    it("should get chats from", async () => {
        await ChatModel.insertMany(conversation.slice(0, 2));

        let results = await chatRepo.getFromTo(sender.id);
        expect(results.length).to.equal(1);
        expect(results[0].sender.id).to.equal(sender.id);
    });

    it("should get chats to", async () => {
        await ChatModel.insertMany(conversation);

        let results = await chatRepo.getFromTo(undefined, sender.id);
        expect(results.length).to.equal(1);
        expect(results[0].receipient.data.id).to.equal(sender.id);
    });

    it("should get chats from and to", async () => {
        await ChatModel.insertMany(conversation);
        let results = await chatRepo.getFromTo(
            sender.id, receipient.data.id);
        expect(results.length).to.equal(1);
        expect(results[0].sender.id).to.equal(sender.id);
        expect(results[0].receipient.data.id)
            .to.equal(receipient.data.id);
    });

    it("should get chats after a specified timestamp",
        async () => {
            await ChatModel.insertMany(conversation);
            let results = await chatRepo.getFromTo(
                null, null, date);
            expect(results.length).to.equal(2);
            expect(results).to.have.nested
                .property('[0].timestamp').to.greaterThan(date);
            expect(results).to.have.nested
                .property('[1].timestamp').to.greaterThan(date);
        });
});