export function holdFunction(callback: Function, numberOfTimes: number) {
    let numberOfTimesCalled = 0;
    return () => {
        numberOfTimesCalled += 1;
        if (numberOfTimesCalled === numberOfTimes) {
            callback();
        }
    }
}