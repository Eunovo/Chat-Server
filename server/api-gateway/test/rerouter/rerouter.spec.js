const mocha = require("mocha");
const { expect } = require("chai");

const { transformUrl } = require("../../src/rerouter/rerouter");

describe("Rerouter", () => {
    it("should transform the url", () => {
        const target = "http://auth.com";
        const key = "/auth";
        let path = "/login";
        const test = () => {
            let testRequest = "http://test.com" + key + path;
            let newRequestUrl = transformUrl(testRequest, key, target);
            expect(newRequestUrl).to.equal(target+path);
        }
        test();
        path = "";
        test();
    });
});