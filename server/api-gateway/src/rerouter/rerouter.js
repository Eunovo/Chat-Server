const axios = require('axios');

module.exports.makeRouter = (key, value) => {
    return async (req, res, next) => {
        try {
            let url = transformUrl(req.originalUrl, key, value);
            let method = req.method;
            let newRequest = {
                url,
                method,
                data: req.body,
                responseType: req.accepts(['json']),
                headers: req.headers
            };   
            let response = await axios.request(newRequest);
            let responseMessage = "";
            if (response) {
                if (response.data) {
                    responseMessage = response.data.message;
                }
            }
            console.log(`Request ${req.originalUrl}, ` +
                `Response Method: ${method}, URL: ${url}, `+
                `Status: ${response.status}, `+
                `Message: ${responseMessage}`);
            return res.status(response.status).json(response.data);
        } catch (err) {
            next(err);
        }
    };
}

const transformUrl = (url, key, target) => {
    let transformedUrl = "";
    let index = url.indexOf(key);
    if (index !== -1) {
        transformedUrl = target + url.substr(
            index + key.length);
    }
    return transformedUrl;
}

module.exports.transformUrl = transformUrl;