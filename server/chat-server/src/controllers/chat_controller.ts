import ApiResponse from '../models/response';
import { ResponseEntity } from '../models/http';
import { chatService } from '../services';

class ChatController {

    async getLatestChats(req: any):
        Promise<ResponseEntity<any>> {
        let { params, user } = req;
        let lastSeen = new Date(params.lastSeen);
        let results = await chatService
            .getChatsTo(user.id, lastSeen);
        let responseBody = ApiResponse.success(
            results, "Latest Chats");
        return {
            statusCode: 200,
            body: responseBody
        }
    }

}

const chatController = new ChatController();
export default chatController;