import chai from "chai";
import mocha from "mocha";

import { checkIfExists } from "../../src/helpers/validators";

const expect = chai.expect;

describe("Validators Test", () => {
    describe("Test checkIfExists", () => {
        it("should return false when value is null or undefined", () => {
            let result = checkIfExists(null);
            expect(result).to.equal(false);
            result = checkIfExists(undefined);
            expect(result).to.equal(false);
        });

        it("should return true when value is a string", () => {
            let result = checkIfExists("novo");
            expect(result).to.equal(true);
        });

        it("should return false when value is an empty string", () => {
            let result = checkIfExists("");
            expect(result).to.equal(false);
        });
    });
});