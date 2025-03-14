package io.github.rafael24595.totp;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class TimeResolverTravelerTest {

    private final double tolerance = 0.01;

    @ParameterizedTest
    @CsvSource({
            "-100000",
            "-10000",
            "-1",
            "0",
            "1",
            "10000",
            "100000",
    })
    void testFastForward(long time) {
        TimeResolverTraveler resolver = new TimeResolverTraveler();
        long timestamp = resolver.fastForward(time);

        long diff = 0;
        if(time != 0) {
            diff = (Instant.now().toEpochMilli() - timestamp - time) / time;
        }

        assertFalse(diff > tolerance);
    }

}
