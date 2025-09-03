package cz.mg.test;

import cz.mg.annotations.classes.Static;
import cz.mg.annotations.requirement.Mandatory;

/**
 * Class for simple performance testing.
 */
public @Static class Performance {
    public static long measure(@Mandatory Runnable service) {
        long start = System.currentTimeMillis();
        service.run();
        return System.currentTimeMillis() - start;
    }

    public static long measure(@Mandatory Runnable runnable, int iterations) {
        long total = 0L;
        for (int i = 0; i < iterations; i++) {
            total += measure(runnable);
        }
        return total / iterations;
    }
}
