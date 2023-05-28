package cz.mg.test.components.builders;

import cz.mg.annotations.classes.Component;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.test.exceptions.AssertException;
import cz.mg.test.components.UnsafeRunnable;

public @Component class CodeAssertion {
    private final @Mandatory UnsafeRunnable runnable;
    private @Optional String message;

    public CodeAssertion(@Mandatory UnsafeRunnable runnable) {
        this.runnable = runnable;
    }

    public CodeAssertion withMessage(@Mandatory String message) {
        this.message = message;
        return this;
    }

    public void doesNotThrowAnyException() {
        assertExceptionNotThrown(runnable, message);
    }

    public void throwsException() {
        throwsException(Exception.class);
    }

    public <E extends Exception> E throwsException(@Mandatory Class<E> type) {
        return assertExceptionThrown(type, runnable, message);
    }

    private static void assertExceptionNotThrown(@Mandatory UnsafeRunnable runnable, @Optional String message) {
        try {
            runnable.run();
        } catch (AssertException e) {
            throw e;
        } catch (Exception e) {
            throw new AssertException(extendMessage(
                "Unexpected exception of type " + e.getClass().getSimpleName() + " with message: " + e.getMessage(),
                message
            ), e);
        }
    }

    private static <E extends Exception> E assertExceptionThrown(
        @Mandatory Class<E> type,
        @Mandatory UnsafeRunnable runnable,
        @Optional String message
    ) {
        try {
            runnable.run();
        } catch (Exception e) {
            if (type.isAssignableFrom(e.getClass())) {
                //noinspection unchecked
                return (E) e;
            } else {
                throw new AssertException(extendMessage(
                    "Expected an exception of type " + type.getSimpleName() +
                        " to be thrown, but got " + e.getClass().getSimpleName() + ".",
                    message
                ), e);
            }
        }

        throw new AssertException(extendMessage(
            "Expected an exception of type " + type.getSimpleName() + " to be thrown, but no exception was thrown.",
            message
        ));
    }

    private static @Mandatory String extendMessage(@Mandatory String mandatoryPart, @Optional String optionalPart) {
        if (optionalPart == null) {
            return mandatoryPart;
        } else {
            return mandatoryPart + " " + optionalPart;
        }
    }
}
