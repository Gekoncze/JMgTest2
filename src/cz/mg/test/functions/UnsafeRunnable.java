package cz.mg.test.functions;

import cz.mg.annotations.classes.Functional;

public @Functional interface UnsafeRunnable {
    void run() throws Exception;
}
