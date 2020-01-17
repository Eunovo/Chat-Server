import HttpStatus from 'http-status-codes';

import ApiError from './api_error';

class OperationFailedError extends ApiError {
    propertyName: string;
    message: string;

    constructor(message?: string) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, "Operation Failed");
        Object.setPrototypeOf(this, new.target.prototype);
        this.message = message;
    }

    getErrors() {
        return [{
            message: this.message,
        }]
    }
}

export default OperationFailedError;