module.exports.makeRouter = (key, value) => {
    return (req, res) => {
        let url = transformUrl(req.url, key, value);
        return Object.freeze({
            url,
            method: req.method,
        });
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