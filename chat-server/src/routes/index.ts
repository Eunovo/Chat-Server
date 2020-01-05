import chatController from "../controllers/chat_controller";
import { makeCallback } from "../helpers/express-utils";

const apiPrefix = "/api/v1";

const router = (app) => {
    app.get(apiPrefix+"/test", (req, res, next) => {
        res.status(200).send("Working");
    });

    app.get(apiPrefix+"/latest/:lastSeen", 
        makeCallback((req) => chatController.getLatestChats(req)));
}

export default router;