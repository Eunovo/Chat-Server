import HttpStatus from 'http-status-codes';

import ApiError from './api_error';

class ConstraintViolationError extends ApiError {
    resourceName: string;
    propertyName: string;
    message: string;

    constructor(resourceName: string, propertyName: string, 
        message?: string) {
        super(HttpStatus.BAD_REQUEST, "Illegal input");
        Object.setPrototypeOf(this, new.target.prototype);
        this.resourceName = resourceName;
        this.propertyName = propertyName;
        this.message = message;
    }

    getErrors() {
        return [{
            resource: this.resourceName,
            field: this.propertyName,
            message: this.message,
        }]
    }
}

export default ConstraintViolationError;