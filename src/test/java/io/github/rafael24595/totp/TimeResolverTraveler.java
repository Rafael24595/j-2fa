package io.github.rafael24595.totp;

import io.github.rafael24595.ITimeResolver;

import java.time.Instant;

public class TimeResolverTraveler implements ITimeResolver {

    private long increment;

    public TimeResolverTraveler() {
        this.increment = 0;
    }

    public long fastForward(long milliseconds) {
        this.increment += milliseconds;
        return this.nowMilliseconds();
    }

    @Override
    public long nowMilliseconds() {
        return Instant.now().toEpochMilli() + increment;
    }

}
