const fs = require('fs');
const express = require('express');
const dotenv = require('dotenv');

const rerouter = require('./rerouter/rerouter');

dotenv.config();

const app = express();

const routesStr = fs.readFileSync('./routes.json');
const routes = JSON.parse(routesStr);
routes.forEach((value, key) => {
    app.use(key, rerouter(key, value));
});

app.use((req, res, err) => {
    // error handler
    res.status(500).json({
        status: "ERROR",
        message: "An error has occurred",
        data: null,
    });
});

const port = process.env.PORT;

app.listen(port, () => {
    console.log("Starting Api Gateway...")
});