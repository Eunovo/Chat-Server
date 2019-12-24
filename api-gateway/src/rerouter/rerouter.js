const axios = require('axios');

module.exports.makeRouter = (key, value) => {
    return async (req, res, next) => {
        try {
            let url = transformUrl(req.originalUrl, key, value);
            let newRequest = {
                url,
                method: req.method,
                data: req.body,
                responseType: req.accepts(['json']),
                headers: {
                    'Authorisation': req.get('authorisation') || ''
                }
            };
            let response = await axios.request(newRequest);
            console.log(req, response);
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