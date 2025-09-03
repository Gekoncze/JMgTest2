package cz.mg.test.components.builders;

import cz.mg.annotations.classes.Component;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.test.exceptions.AssertException;

public abstract @Component class FluentAssertion {
    private @Optional String message;

    public @Mandatory FluentAssertion withMessage(@Mandatory String message) {
        this.message = message;
        return this;
    }

    protected @Mandatory AssertException createException(@Mandatory String specificMessage) {
        String firstLine = message == null ? "" : message + "\n";
        return new AssertException(firstLine + specificMessage);
    }

    protected @Mandatory AssertException createException(@Mandatory String specificMessage, @Mandatory Exception cause) {
        String firstLine = message == null ? "" : message + "\n";
        return new AssertException(firstLine + specificMessage, cause);
    }

    protected @Mandatory String[] getNames(@Mandatory Class<?> first, @Mandatory Class<?> second) {
        String firstName = first.getSimpleName();
        String secondName = second.getSimpleName();

        if (firstName.equals(secondName)) {
            firstName = first.getName();
            secondName = second.getName();
        }

        return new String[] {
            firstName,
            secondName
        };
    }
}
