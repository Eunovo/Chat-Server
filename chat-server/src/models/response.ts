export default class Response {
    private success: string = "SUCCESS";
    private error: string = "ERROR";

    static success(data: any, message: string) {
        return  {
            status: this.success,
            message,
            data,
            errors: []
        };
    }

    static error(message: string, errors?: any[]) {
        return {
            status: this.error,
            message,
            data: null,
            errors: errors || [],
        }
    }
}