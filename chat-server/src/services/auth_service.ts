import { UserInfo } from "../data/chat";

export default interface AuthService {
    authenticate(token: string): Promise<UserInfo>;
}

export class AuthServiceImpl implements AuthService {
    authenticate(token: string): Promise<UserInfo> {
        throw new Error("Method not implemented.");
    }
}