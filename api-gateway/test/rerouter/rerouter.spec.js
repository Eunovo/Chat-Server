const mocha = require("mocha");
const { expect } = require("chai");

const { makeRouter } = require("../../src/rerouter/rerouter");

describe("Rerouter", () => {
    it("should route the request as specified in the routes input", () => {
        const target = "http://auth.com";
        const routes = new Map([
            ["auth/", target]
        ]);
        const path = "/login";
        const testRequest = {
            url: "http://test.com/auth" + path
        };
        const route = makeRouter(routes);
        let newRequest = route(testRequest);
        expect(newRequest.url).to.equal(target+path);
    });
});