package cz.mg.test.components.builders;

import cz.mg.annotations.classes.Component;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.functions.FormatFunction;
import cz.mg.functions.FormatFunctions;
import cz.mg.test.exceptions.AssertException;
import cz.mg.test.components.functions.CompareFunction;
import cz.mg.test.components.functions.common.DefaultCompareFunction;

public @Component class BinaryObjectAssertion<T> {
    private final @Optional T expectation;
    private final @Optional T reality;
    private @Mandatory CompareFunction<T> compareFunction = new DefaultCompareFunction<>();
    private @Mandatory FormatFunction<T> formatFunction = FormatFunctions.TO_STRING();
    private @Optional String message;

    public BinaryObjectAssertion(@Optional T expectation, @Optional T reality) {
        this.expectation = expectation;
        this.reality = reality;
    }

    public BinaryObjectAssertion<T> withCompareFunction(@Mandatory CompareFunction<T> compareFunction) {
        this.compareFunction = compareFunction;
        return this;
    }

    public BinaryObjectAssertion<T> withPrintFunction(@Mandatory FormatFunction<T> formatFunction) {
        this.formatFunction = formatFunction;
        return this;
    }

    public BinaryObjectAssertion<T> withMessage(@Mandatory String message) {
        this.message = message;
        return this;
    }

    public void areEqual() {
        assertEquals(expectation, reality, compareFunction, formatFunction, message);
    }

    public void areNotEqual() {
        assertNotEquals(expectation, reality, compareFunction, formatFunction, message);
    }

    public void areSame() {
        if (expectation != reality) {
            throw new AssertException(extendMessage(
                "Expected " + (expectation == null ? "null" : formatFunction.format(expectation)) +
                    " and " + (reality == null ? "null" : formatFunction.format(reality)) +
                    " to be the same object.",
                message
            ));
        }
    }

    public void areNotSame() {
        if (expectation == reality) {
            throw new AssertException(extendMessage(
                "Did not expect " + (expectation == null ? "null" : formatFunction.format(expectation)) +
                    " and " + (reality == null ? "null" : formatFunction.format(reality)) +
                    " to be the same object.",
                message
            ));
        }
    }

    private static <T> void assertEquals(
        @Optional T expectation,
        @Optional T reality,
        @Mandatory CompareFunction<T> compareFunction,
        @Mandatory FormatFunction<T> formatFunction,
        @Optional String message
    ) {
        if (expectation != reality) {
            if (expectation == null) {
                throw new AssertException(extendMessage(
                    "Expected null, but got " + formatFunction.format(reality) + ".",
                    message
                ));
            }

            if (reality == null) {
                throw new AssertException(extendMessage(
                    "Expected " + formatFunction.format(expectation) + ", but got null.",
                    message
                ));
            }

            if (!compareFunction.equals(expectation, reality)) {
                throw new AssertException(extendMessage(
                    "Expected " + formatFunction.format(expectation) +
                        ", but got " + formatFunction.format(reality) + ".",
                    message
                ));
            }
        }
    }

    private static <T> void assertNotEquals(
        @Optional T wrong,
        @Optional T reality,
        @Mandatory CompareFunction<T> compareFunction,
        @Mandatory FormatFunction<T> formatFunction,
        @Optional String message
    ) {
        if (reality == null && wrong == null) {
            throw new AssertException(extendMessage(
                "Unexpected null value.",
                message
            ));
        }

        if (reality == null || wrong == null) {
            return;
        }

        if (compareFunction.equals(wrong, reality)) {
            throw new AssertException(extendMessage(
                "Did not expect " + formatFunction.format(wrong) +
                    ", but got " + formatFunction.format(reality) + ".",
                message
            ));
        }
    }

    private static @Mandatory String extendMessage(@Mandatory String mandatoryPart, @Optional String optionalPart) {
        if (optionalPart == null) {
            return mandatoryPart;
        } else {
            return mandatoryPart + " " + optionalPart;
        }
    }
}
