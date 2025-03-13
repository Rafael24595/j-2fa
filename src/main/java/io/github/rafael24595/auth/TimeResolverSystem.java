package io.github.rafael24595.auth;

import io.github.rafael24595.ITimeResolver;

import java.time.Instant;

public class TimeResolverSystem implements ITimeResolver {

    @Override
    public long nowMilliseconds() {
        return Instant.now().toEpochMilli();
    }

}
