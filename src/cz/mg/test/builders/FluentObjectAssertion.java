package cz.mg.test.builders;

import cz.mg.annotations.classes.Component;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.functions.EqualsFunction;
import cz.mg.functions.FormatFunction;
import cz.mg.functions.FormatFunctions;
import cz.mg.test.exceptions.AssertException;
import cz.mg.test.functions.DefaultFunctions;

public @Component class FluentObjectAssertion<T> {
    private final @Optional T reality;
    private @Optional String message;
    private @Mandatory EqualsFunction<T> equalsFunction = DefaultFunctions.EQUALS();
    private @Mandatory FormatFunction<T> formatFunction = FormatFunctions.TO_STRING();

    public FluentObjectAssertion(@Optional T reality) {
        this.reality = reality;
    }

    public @Mandatory FluentObjectAssertion<T> withMessage(@Mandatory String message) {
        this.message = message;
        return this;
    }

    public @Mandatory FluentObjectAssertion<T> withEqualsFunction(@Mandatory EqualsFunction<T> equalsFunction) {
        this.equalsFunction = equalsFunction;
        return this;
    }

    public @Mandatory FluentObjectAssertion<T> withFormatFunction(@Mandatory FormatFunction<T> formatFunction) {
        this.formatFunction = formatFunction;
        return this;
    }

    public void isNull() {
        if (reality != null) {
            throw createException("Expected null, but got " + formatFunction.formatOptional(reality) + ".");
        }
    }

    public void isNotNull() {
        if (reality == null) {
            throw createException("Expected non-null, but got null.");
        }
    }

    public void isSameAs(@Optional T expectation) {
        if (expectation != reality) {
            throw createException(
                "Expected " + formatFunction.formatOptional(expectation) +
                    " to be the same as " + formatFunction.formatOptional(reality) + "."
            );
        }
    }

    public void isNotSameAs(@Optional T expectation) {
        if (expectation == reality) {
            throw createException(
                "Did not expect " + formatFunction.formatOptional(expectation) +
                    " to be the same as " + formatFunction.formatOptional(reality) + "."
            );
        }
    }

    public void isEqualTo(@Optional T expectation) {
        if (!equalsFunction.equalsOptional(expectation, reality)) {
            throw createException(
                "Expected " + formatFunction.formatOptional(expectation) + ", "
                    + "but got " + formatFunction.formatOptional(reality) + "."
            );
        }
    }

    public void isNotEqualTo(@Optional T expectation) {
        if (equalsFunction.equalsOptional(expectation, reality)) {
            throw createException(
                "Did not expect " + formatFunction.formatOptional(expectation) + ", "
                    + "but got " + formatFunction.formatOptional(reality) + "."
            );
        }
    }

    @SuppressWarnings("rawtypes")
    public void isInstanceOf(@Mandatory Class expectedClass) {
        if (reality == null) {
            throw createException(
                "Expected an instance of type " + expectedClass.getSimpleName() + ", but got null."
            );
        }

        if (!expectedClass.isInstance(reality)) {
            String[] names = Names.get(expectedClass, reality.getClass());
            throw createException(
                "Expected an instance of " + names[0] + ", "
                    + "but got instance of " + names[1] + "."
            );
        }
    }

    @SuppressWarnings("rawtypes")
    public void isNotInstanceOf(@Mandatory Class expectedClass) {
        if (reality == null) {
            return;
        }

        if (expectedClass.isInstance(reality)) {
            String expectedClassName = expectedClass.getSimpleName();
            String actualClassName = reality.getClass().getSimpleName();
            if (expectedClassName.equals(actualClassName)) {
                expectedClassName = expectedClass.getName();
                actualClassName = reality.getClass().getName();
            }
            throw createException(
                "Did not expect an instance of " + expectedClassName + ", "
                    + "but got instance of " + actualClassName + "."
            );
        }
    }

    private @Mandatory AssertException createException(@Mandatory String specificMessage) {
        return new ExceptionBuilder()
            .addMessage(message)
            .addMessage(specificMessage)
            .build();
    }
}
