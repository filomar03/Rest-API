package it.unimib.sd2024;

import java.time.Duration;
import java.time.Instant;

public class LockUtils {
    private static final Duration LOCK_DURATION = Duration.ofMinutes(10);

    public static Instant computeLockEnd(Instant t) {
        return t.plus(LOCK_DURATION);
    }
}
