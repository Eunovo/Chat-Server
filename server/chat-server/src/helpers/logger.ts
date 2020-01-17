import winston, { format } from 'winston';

const consoleTransport = new winston.transports.Console()
const consoleLogger = winston.createLogger({
    transports: [consoleTransport]
});

const { combine, timestamp, printf } = format;
const logFormat = printf(({ level, message, timestamp }) => {
    return `${timestamp} ${level}: ${message}`;
});


const fileLogger = winston.createLogger({
    level: 'info',
    format: combine(
        timestamp(),
        logFormat
    ),
    transports: [
        new winston.transports.File({ filename: 'error.log', level: 'error' }),
        new winston.transports.File({ filename: 'combined.log' })
    ]
});

function logRequestInProduction(req, res, next) {
    fileLogger.info(req.url)
    next()
}

function logRequestInDev(req, res, next) {
    if (process.env.DEBUG === 'true') {
        consoleLogger.info(req.url);
    }
    next()
}

function logErrorInProduction(err, req, res, next) {
    fileLogger.error(err.message);
    next(err);
}

function logErrorInDev(err, req, res, next) {
    consoleLogger.error(err.message);
    next(err);
}

export function logRequest(req, res, next) {
    if (process.env.NODE_ENV === 'production') {
        logRequestInProduction(req, res, next);
    } else {
        logRequestInDev(req, res, next);
    }
}

export function logError(err, req, res, next) {
    if (process.env.NODE_ENV === 'production') {
        logErrorInProduction(err, req, res, next);
    } else {
        logErrorInDev(err, req, res, next);
    }
}