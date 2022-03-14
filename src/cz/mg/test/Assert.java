package cz.mg.test;

import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;

@SuppressWarnings("unchecked")
public class Assert {
    public static void assertNotNull(@Optional Object object) {
        if (object == null) {
            throw new AssertException("Unexpected null value.");
        }
    }

    public static void assertEquals(@Optional Object expectation, @Optional Object reality) {
        if(expectation != reality) {
            if (expectation == null || reality == null) {
                throw new AssertException("Expected " + expectation + ", but got " + reality + ".");
            }

            if (!expectation.equals(reality)) {
                throw new AssertException("Expected " + expectation + ", but got " + reality + ".");
            }
        }
    }

    public static <T extends Exception> T assertExceptionThrown(@Mandatory Class<T> type, @Mandatory Runnable runnable) {
        try {
            runnable.run();
        } catch (Exception e) {
            if (e.getClass().equals(type)) {
                return (T) e;
            }
        }

        throw new AssertException("Expected an exception of type " + type.getSimpleName() + " to be thrown.");
    }

    public static void assertExceptionNotThrown(@Mandatory UnsafeRunnable runnable) {
        try {
            runnable.run();
        } catch (AssertException e) {
            throw e;
        } catch (Exception e) {
            throw new AssertException("Unexpected exception of type " + e.getClass().getSimpleName() + " with message: " + e.getMessage());
        }
    }

    public interface UnsafeRunnable {
        void run() throws Exception;
    }
}
