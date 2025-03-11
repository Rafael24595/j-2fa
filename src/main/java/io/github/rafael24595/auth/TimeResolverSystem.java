package io.github.rafael24595.auth;

import io.github.rafael24595.ITimeResolver;

public class TimeResolverSystem implements ITimeResolver {

    @Override
    public long nowMilliseconds() {
        return System.currentTimeMillis();
    }

}
