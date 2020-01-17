const fs = require('fs');
const express = require('express');
const dotenv = require('dotenv');

const { makeRouter: rerouter } = require('./rerouter/rerouter');

dotenv.config();

const app = express();
app.use(express.json())

const routesStr = fs.readFileSync(`${__dirname}/routes.json`);
const routes = new Map(JSON.parse(routesStr));
routes.forEach((value, key) => {
    app.use(key, rerouter(key, value));
});

app.use((err, req, res, next) => {
    console.log(err);
    next(err);
});

app.use((err, req, res, next) => {
    if (err.response) {
        res.status(err.response.status).json(err.response.data);
    } else {
        next(err);
    }
});

app.use((err, req, res, next) => {
    res.status(500).json({
        status: "ERROR",
        message: "An error has occurred",
        data: err.message,
    });
});

const isProduction = process.env.NODE_ENV === "production";
const port = isProduction ? process.env.PORT : 3000;

app.listen(port, () => {
    console.log(`Starting Api Gateway on port:${port}`)
});