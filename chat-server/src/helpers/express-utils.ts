import httpstatus from "http-status-codes";

import ApiError from "../errors/api_error";
import Response from "../models/response";
import { authService } from "../services";
import { RequestEntity } from "../models/http";
import { Request } from "express";

export const makeCallback = (handler: (body: RequestEntity<any>) => Promise<any>) => {
    return async (req, res, next) => {
        let body = {
            url: req.url,
            body: req.body,
            params: req.params,
            query: req.query,
            user: req.user,
        };
        try {
            let response = await handler(body);
            return res.status(response.statusCode)
                .json(response.body);
        } catch (e) {
            next(e);
        }
    }
}

export const authMiddleware = async (req: Request, res, next) => {
    let authorization = req.headers.authorization;
    let forbid = () => res.status(httpstatus.FORBIDDEN)
        .json(Response.error("Unauthorised"));
    if (!authorization) {
        forbid();
        return;
    }
    let tokens = authorization.split(" ");
    if (tokens[0].toLowerCase() === "bearer") {
        try {
            let user = await authService.authenticate(tokens[1]);
            req['user'] = user;
            next();
        } catch (err) {
            forbid();
        }
    } else {
        forbid();
    }
};

export const errorHandler = async (err, req, res, next) => {
    if (err instanceof SyntaxError) {
        res.status(httpstatus.BAD_REQUEST)
            .json(Response.error("Invalid JSON", []));
    } else if (err instanceof ApiError ) {
        res.status(err.status)
            .json(Response.error(err.message, err.getErrors()));
    } else {
        next(err);
    }
}