interface HttpEntity <T> {
    body: T;
}

export interface RequestEntity<T> extends HttpEntity<T> {
    url: string;
    params: any;
    query: any;
    user: any;
}

export interface ResponseEntity<T> extends HttpEntity<T> {
    statusCode: number;
}