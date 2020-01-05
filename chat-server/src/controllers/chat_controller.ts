import { chatService } from '../services';

class ChatController {

    getLatestChats(req: any): Promise<any> {
        let { lastSeen } = req.query;

        return Promise.resolve(null);
    }

}

const chatController = new ChatController();
export default chatController;