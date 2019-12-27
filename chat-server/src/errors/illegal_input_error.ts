import HttpStatus from 'http-status-codes';

import ApiError from './api_error';

class IllegalInputError extends ApiError {
    message: string;

    constructor(message?: string) {
        super(HttpStatus.BAD_REQUEST, "Illegal input");
        Object.setPrototypeOf(this, new.target.prototype);
        this.message = message;
    }

    getErrors() {
        return [{
            message: this.message,
        }]
    }
}

export default IllegalInputError;