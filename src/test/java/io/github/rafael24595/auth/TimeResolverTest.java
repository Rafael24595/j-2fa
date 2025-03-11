package io.github.rafael24595.auth;

import io.github.rafael24595.ITimeResolver;

class TimeResolverTest implements ITimeResolver {

    private long increment;

    public TimeResolverTest() {
        this.increment = 0;
    }

    public long fastForward(long milliseconds) {
        this.increment += milliseconds;
        return this.nowMilliseconds();
    }

    @Override
    public long nowMilliseconds() {
        return System.currentTimeMillis() + increment;
    }

}
