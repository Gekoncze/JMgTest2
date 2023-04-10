package cz.mg.test;

import cz.mg.annotations.requirement.Mandatory;

public class Performance {
    public static long measure(@Mandatory Runnable service) {
        long start = System.currentTimeMillis();
        service.run();
        return System.currentTimeMillis() - start;
    }
}