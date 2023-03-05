package cz.mg.test;

import cz.mg.annotations.classes.Utility;

public @Utility interface UnsafeRunnable {
    void run() throws Exception;
}
