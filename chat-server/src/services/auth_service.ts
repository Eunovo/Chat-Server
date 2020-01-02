import axios from "axios";

import { UserInfo } from "../data/chat";

export default interface AuthService {
    authenticate(token: string): Promise<UserInfo>;
}

export class AuthServiceImpl implements AuthService {
    async authenticate(token: string): Promise<UserInfo> {
        try {
            return this.doAuthenticate(token);
        } catch (err) {
            if (err.response) {
                throw new Error(err.response.message);
            }
            throw new Error("Failed");
        }
    }

    async doAuthenticate(token: string) {
        let result = await axios.post(process.env.AUTH_SERVICE_URL, {
            token
        });
        let { status, message, data } = result.data;
        if (status === "SUCCESS") {
            return data;
        } else if (status === "ERROR") {
            throw new Error(message);
        } else {
            throw new Error("Invalid Status Received");
        }
    }
}