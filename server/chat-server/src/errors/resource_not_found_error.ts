import HttpStatus from 'http-status-codes';

import ApiError from './api_error';

class ResourceNotFoundError extends ApiError {
    resourceName: string;
    query: string;

    constructor(resourceName: string, query: string) {
        let message = `${resourceName} <${query}> not found`;
        super(HttpStatus.NOT_FOUND, message);
        Object.setPrototypeOf(this, new.target.prototype);
        this.resourceName = resourceName;
        this.query = query;
    }

    getErrors() {
        return [{
            resource: this.resourceName,
            query: this.query,
        }]
    }
}

export default ResourceNotFoundError;