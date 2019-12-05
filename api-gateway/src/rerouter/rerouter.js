module.exports.makeRouter = (routes) => {
    return (req) => {
        let url = transformUrl(req.url, routes);
        return {
            url
        };
    };
}

const transformUrl = (url, routes) => {
    let transformedUrl = "";
    routes.forEach((value, key) => {
        let index = url.indexOf(key);
        if (index !== -1) {
            transformedUrl = value + url.substr(
                index + key.length - 1);
        }
    });
    return transformedUrl;
}