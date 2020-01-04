export default class Response {
    private success: string = "SUCCESS";
    private error: string = "ERROR";

    static success(data: any, message: string) {
        return  {
            status: this.success,
            code: 200,
            message,
            data,
            errors: []
        };
    }

    static error(message: string, code: number) {
        return {
            status: this.error,
            code,
            message,
            data: null,
            errors: [],
        }
    }
}