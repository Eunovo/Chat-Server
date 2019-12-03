const mocha = require("mocha");
const { expect } = require("chai");

const { makeRouter, urlMatcher } = require("../../src/rerouter/rerouter");

describe("Rerouter", () => {
    it("should route the request as specified in the routes input", () => {
        const target = "http://auth.com";
        const routes = new Map([
            ["auth/*", target]
        ]);
        const path = "/login";
        const testRequest = {
            url: "http://test.com/auth" + path
        };
        const route = makeRouter(routes);
        let newRequest = route(testRequest);
        expect(newRequest.url).to.equal(target+path);
    });

    describe("Url matcher", () => {
        it("should match '*' at the end", () => {
            let test = "auth/*";
            let url = "auth/t";
            expect(urlMatcher(test, url)).to.eq(true);
            url = "bauth/b";
            expect(urlMatcher(test, url)).to.eq(false);
        });
    });
});