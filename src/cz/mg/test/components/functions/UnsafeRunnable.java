package cz.mg.test.components.functions;

import cz.mg.annotations.classes.Functional;

public @Functional interface UnsafeRunnable {
    void run() throws Exception;
}
