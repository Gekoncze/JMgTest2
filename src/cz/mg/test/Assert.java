package cz.mg.test;

import cz.mg.annotations.classes.Utility;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;

import java.util.Iterator;

@SuppressWarnings("unchecked")
public class Assert {
    public static void assertNull(@Optional Object object) {
        if (object != null) {
            throw new AssertException("Unexpected nonnull value.");
        }
    }

    public static void assertNotNull(@Optional Object object) {
        if (object == null) {
            throw new AssertException("Unexpected null value.");
        }
    }

    public static void assertSame(@Optional Object expectation, @Optional Object reality) {
        if (expectation != reality) {
            throw new AssertException("Expected " + expectation + " and " + reality + " to be the same object.");
        }
    }

    public static void assertNotSame(@Optional Object expectation, @Optional Object reality) {
        if (expectation == reality) {
            throw new AssertException("Expected " + expectation + " and " + reality + " to not be the same object.");
        }
    }

    public static void assertEquals(@Optional Object expectation, @Optional Object reality) {
        if (expectation != reality) {
            if (expectation == null || reality == null) {
                throw new AssertException("Expected " + expectation + ", but got " + reality + ".");
            }

            if (expectation instanceof Number && reality instanceof Number) {
                assertEqualsNumerically((Number) expectation, (Number) reality);
            } else {
                if (!expectation.equals(reality)) {
                    throw new AssertException("Expected " + expectation + ", but got " + reality + ".");
                }
            }
        }
    }

    public static <T> void assertEquals(
        @Optional T expectation,
        @Optional T reality,
        @Mandatory CompareFunction<T> compareFunction,
        @Mandatory ToStringFunction<T> stringFunction
    ) {
        if (expectation != reality) {
            if (expectation == null) {
                throw new AssertException("Expected null, but got " + stringFunction.toString(reality) + ".");
            }

            if (reality == null) {
                throw new AssertException("Expected " + stringFunction.toString(expectation) + ", but got null.");
            }

            if (!compareFunction.equals(expectation, reality)) {
                throw new AssertException(
                    "Expected " + stringFunction.toString(expectation) +
                        ", but got " + stringFunction.toString(reality) + "."
                );
            }
        }
    }

    private static void assertEqualsNumerically(@Mandatory Number expectation, @Mandatory Number reality) {
        long expectationLong = convert(expectation);
        long realityLong = convert(reality);
        if (expectationLong != realityLong) {
            throw new AssertException("Expected " + expectation + ", but got " + reality + ".");
        }
    }

    private static long convert(@Mandatory Number number) {
        if (number instanceof Long) {
            return (long) number;
        }

        if (number instanceof Integer) {
            return (int) number;
        }

        if (number instanceof Short) {
            return (short) number;
        }

        throw new UnsupportedOperationException(
            "Unsupported number type for comparison: " + number.getClass().getSimpleName() + "."
        );
    }

    public static void assertExceptionThrown(@Mandatory Runnable runnable) {
        assertExceptionThrown(Exception.class, runnable);
    }

    public static <T extends Exception> T assertExceptionThrown(
        @Mandatory Class<T> type,
        @Mandatory Runnable runnable
    ) {
        try {
            runnable.run();
        } catch (Exception e) {
            if (type.isAssignableFrom(e.getClass())) {
                return (T) e;
            } else {
                throw new AssertException(
                    "Expected an exception of type " + type.getSimpleName() +
                        " to be thrown, but got " + e.getClass().getSimpleName() + ".", e
                );
            }
        }

        throw new AssertException(
            "Expected an exception of type " + type.getSimpleName() + " to be thrown, but no exception was thrown."
        );
    }

    public static void assertExceptionNotThrown(@Mandatory UnsafeRunnable runnable) {
        try {
            runnable.run();
        } catch (AssertException e) {
            throw e;
        } catch (Exception e) {
            throw new AssertException(
                "Unexpected exception of type " + e.getClass().getSimpleName() + " with message: " + e.getMessage(), e
            );
        }
    }

    public static <T> void assertEquals(
        @Optional Iterable<T> expectation,
        @Optional Iterable<T> reality,
        @Mandatory CompareFunction<T> compareFunction,
        @Mandatory ToStringFunction<T> toStringFunction
    ) {
        if (expectation == reality) {
            return;
        }

        if (expectation == null) {
            throw new AssertException("Expected list to be null, but was not null.");
        }

        if (reality == null) {
            throw new AssertException("Expected list to not be null, but was null.");
        }

        int expectedCount = count(expectation);
        int actualCount = count(reality);

        if (expectedCount != actualCount) {
            throw new AssertException("Expected list count to be " + expectedCount + ", but was " + actualCount + ".");
        }

        int i = -1;
        Iterator<T> expectationIterator = expectation.iterator();
        Iterator<T> realityIterator = reality.iterator();
        while (expectationIterator.hasNext() && realityIterator.hasNext()) {
            i++;
            @Optional T expectedItem = expectationIterator.next();
            @Optional T actualItem = realityIterator.next();

            if (expectedItem == actualItem) {
                continue;
            }

            if (expectedItem == null) {
                throw new AssertException(
                    "Expected item at " + i + " to be null, but was " + toStringFunction.toString(actualItem) + "."
                );
            }

            if (actualItem == null) {
                throw new AssertException(
                    "Expected item at " + i + " to be " + toStringFunction.toString(expectedItem) + ", but was null."
                );
            }

            if (!compareFunction.equals(expectedItem, actualItem)) {
                throw new AssertException(
                    "Expected item at " + i + " to be " + toStringFunction.toString(expectedItem) + ", " +
                        "but was " + toStringFunction.toString(actualItem) + "."
                );
            }
        }
    }

    private static <T> int count(Iterable<T> iterable) {
        int count = 0;
        for (T ignored : iterable) {
            count++;
        }
        return count;
    }

    public @Utility interface CompareFunction<T> {
        boolean equals(@Mandatory T a, @Mandatory T b);
    }

    public @Utility interface ToStringFunction<T> {
        String toString(@Mandatory T a);
    }
}
