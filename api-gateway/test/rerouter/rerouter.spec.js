const mocha = require("mocha");
const { expect } = require("chai");

const { transformUrl } = require("../../src/rerouter/rerouter");

describe("Rerouter", () => {
    it("should transform the url", () => {
        const target = "http://auth.com";
        const key = "/auth";
        const path = "/login";
        const testRequest = "http://test.com/auth"+path;
        let newRequestUrl = transformUrl(testRequest, key, target);
        expect(newRequestUrl).to.equal(target+path);
    });
});