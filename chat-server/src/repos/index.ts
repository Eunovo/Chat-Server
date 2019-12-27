import ChatRepo from "./chat_repo";
import MongooseChatRepo from "./chat_repo_impl";

export const chatRepo: ChatRepo =  new MongooseChatRepo();