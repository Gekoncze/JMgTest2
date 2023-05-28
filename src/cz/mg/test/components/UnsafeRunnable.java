package cz.mg.test.components;

import cz.mg.annotations.classes.Component;

public @Component interface UnsafeRunnable {
    void run() throws Exception;
}
