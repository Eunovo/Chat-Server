module.exports.makeRouter = (routes) => {
    return (req) => {
        let url = transformUrl(req.url);
        return {
            url: "http://auth.com/login"
        };
    };
}

const transformUrl = (url, routes) => {
    return "http://auth.com/login";
}

module.exports.urlMatcher = (matcher, url) => {
    let indexOfStar = matcher.indexOf("*");
    if (matcher.endsWith('*')) {
        let matcherString = matcher.substring(0, indexOfStar);
        return url.startsWith(matcherString);
    } else if (matcher.startsWith('*')) {
        let matcherString = matcher.substr(1);
        return url.endsWith(matcherString);
    } else {
        let preffix = matcher.substring(0, indexOfStar);
        let suffix = matcher.substr(indexOfStar + 1);
        return url.startsWith(preffix) && url.endsWith(suffix);
    }
}