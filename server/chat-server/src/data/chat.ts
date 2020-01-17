import ConstraintViolationError from "../errors/contraint_violation_error";
import { checkIfExists } from "../helpers/validators";

class Chat {

    id: Object;
    sender: UserInfo;
    receipient: Receipient;
    message: Message;
    timestamp: Date;
    createdAt: Date;

    constructor(id: Object, sender: UserInfo,
        receipient: Receipient, message: Message, timestamp: Date) {
        this.id = id;
        this.sender = sender;
        this.receipient = receipient;
        this.message = message;
        this.timestamp = timestamp;
    }

    validate() {
        let propertiesToValidate = ["sender",
            "receipient", "message", "timestamp"];
        for (let i = 0; i < propertiesToValidate.length; i++) {
            if (!checkIfExists(this[propertiesToValidate[i]])) {
                this.throwRequiredError(propertiesToValidate[i]);
            }
        }

        this.validateProperty("sender");
        this.validateProperty("receipient");
        this.validateProperty("message");
    }

    private validateProperty(property) {
        try {
            this[property].validate();
        } catch (e) {
            throw new ConstraintViolationError(
                "Chat", property, `${property} is invalid: ${e.message}`
            );
        }
    }

    private throwRequiredError(property: string) {
        throw new ConstraintViolationError(
            "Chat", property, `${property} is required`
        );
    }
}

export class Receipient {
    type: "single";
    data: UserInfo;

    constructor(type: "single", data: UserInfo) {
        this.type = type;
        this.data = data;
    }

    validate() {
        if (!checkIfExists(this.type)) {
            throw new ConstraintViolationError(
                "Receipient", "type", "type is required"
            );
        }

        if (!checkIfExists(this.data)) {
            throw new ConstraintViolationError(
                "Receipient", "data", "data is required"
            );
        }

        try {
            this.data.validate();
        } catch (e) {
            throw new ConstraintViolationError(
                "Receipient", "data", `data is invalid: ${e.message}`
            );
        }
    }
}

export class UserInfo {
    id: Object;
    username: string;

    constructor(id: Object, username: string) {
        this.id = id;
        this.username = username;
    }

    validate() {
        if (!checkIfExists(this.id)) {
            throw new ConstraintViolationError(
                "UserInfo", "id", "User Id is required");
        }

        if (!checkIfExists(this.username)) {
            throw new ConstraintViolationError(
                "UserInfo", "username", "Username is required");
        }
    }
}

export class Message {
    type: "text";
    data: any;

    constructor(type: "text", data: any) {
        this.type = type;
        this.data = data;
    }

    validate() {
        if (!checkIfExists(this.type)) {
            throw new ConstraintViolationError(
                "Message", "type", "type is required"
            );
        }

        if (!checkIfExists(this.data)) {
            throw new ConstraintViolationError(
                "Message", "data", "data is required"
            );
        }
    }
}

export default Chat;