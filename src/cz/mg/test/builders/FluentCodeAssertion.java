package cz.mg.test.builders;

import cz.mg.annotations.classes.Component;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.test.exceptions.AssertException;
import cz.mg.test.functions.UnsafeRunnable;

public @Component class FluentCodeAssertion {
    private final @Mandatory UnsafeRunnable runnable;
    private @Optional String message;

    public FluentCodeAssertion(@Mandatory UnsafeRunnable runnable) {
        this.runnable = runnable;
    }

    public @Mandatory FluentCodeAssertion withMessage(@Mandatory String message) {
        this.message = message;
        return this;
    }

    public void doesNotThrowAnyException() {
        try {
            runnable.run();
        } catch (AssertException e) {
            throw e;
        } catch (Exception e) {
            throw createException(
                "Unexpected exception of type " + e.getClass().getSimpleName() + " "
                    + "with message: " + e.getMessage() + ".", e
            );
        }
    }

    public void throwsException() {
        throwsException(Exception.class);
    }

    @SuppressWarnings("unchecked")
    public <E extends Exception> E throwsException(@Mandatory Class<E> type) {
        try {
            runnable.run();
        } catch (Exception e) {
            if (type.isAssignableFrom(e.getClass())) {
                return (E) e;
            } else {
                String[] names = Names.get(type, e.getClass());
                throw createException(
                    "Expected an exception of type " + names[0] + " to be thrown, "
                        + "but got " + names[1] + ".", e
                );
            }
        }

        throw createException(
            "Expected an exception of type " + type.getSimpleName() + " to be thrown, "
                + "but no exception was thrown.", null
        );
    }

    private @Mandatory AssertException createException(@Mandatory String specificMessage, @Optional Exception cause) {
        return new ExceptionBuilder()
            .addMessage(message)
            .addMessage(specificMessage)
            .withCause(cause)
            .build();
    }
}
