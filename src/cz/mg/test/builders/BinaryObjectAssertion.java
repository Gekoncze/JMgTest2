package cz.mg.test.builders;

import cz.mg.annotations.classes.Utility;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.test.AssertException;
import cz.mg.test.functions.CompareFunction;
import cz.mg.test.functions.PrintFunction;
import cz.mg.test.functions.common.DefaultCompareFunction;

import java.util.Objects;

public @Utility class BinaryObjectAssertion<T> {
    private final @Optional T expectation;
    private final @Optional T reality;
    private @Mandatory CompareFunction<T> compareFunction = new DefaultCompareFunction<>();
    private @Mandatory PrintFunction<T> printFunction = Objects::toString;
    private @Optional String message;

    public BinaryObjectAssertion(@Optional T expectation, @Optional T reality) {
        this.expectation = expectation;
        this.reality = reality;
    }

    public BinaryObjectAssertion<T> withCompareFunction(@Mandatory CompareFunction<T> compareFunction) {
        this.compareFunction = compareFunction;
        return this;
    }

    public BinaryObjectAssertion<T> withPrintFunction(@Mandatory PrintFunction<T> printFunction) {
        this.printFunction = printFunction;
        return this;
    }

    public BinaryObjectAssertion<T> withMessage(@Mandatory String message) {
        this.message = message;
        return this;
    }

    public void areEqual() {
        assertEquals(expectation, reality, compareFunction, printFunction, message);
    }

    public void areNotEqual() {
        assertNotEquals(expectation, reality, compareFunction, printFunction, message);
    }

    public void areSame() {
        if (expectation != reality) {
            throw new AssertException(extendMessage(
                "Expected " + (expectation == null ? "null" : printFunction.toString(expectation)) +
                    " and " + (reality == null ? "null" : printFunction.toString(reality)) +
                    " to be the same object.",
                message
            ));
        }
    }

    public void areNotSame() {
        if (expectation == reality) {
            throw new AssertException(extendMessage(
                "Did not expect " + (expectation == null ? "null" : printFunction.toString(expectation)) +
                    " and " + (reality == null ? "null" : printFunction.toString(reality)) +
                    " to be the same object.",
                message
            ));
        }
    }

    private static <T> void assertEquals(
        @Optional T expectation,
        @Optional T reality,
        @Mandatory CompareFunction<T> compareFunction,
        @Mandatory PrintFunction<T> printFunction,
        @Optional String message
    ) {
        if (expectation != reality) {
            if (expectation == null) {
                throw new AssertException(extendMessage(
                    "Expected null, but got " + printFunction.toString(reality) + ".",
                    message
                ));
            }

            if (reality == null) {
                throw new AssertException("Expected " + printFunction.toString(expectation) + ", but got null.");
            }

            if (!compareFunction.equals(expectation, reality)) {
                throw new AssertException(
                    "Expected " + printFunction.toString(expectation) +
                        ", but got " + printFunction.toString(reality) + "."
                );
            }
        }
    }

    private static <T> void assertNotEquals(
        @Optional T wrong,
        @Optional T reality,
        @Mandatory CompareFunction<T> compareFunction,
        @Mandatory PrintFunction<T> printFunction,
        @Optional String message
    ) {
        if (reality == wrong) {
            if (reality == null) {
                throw new AssertException(extendMessage(
                    "Unexpected null value.",
                    message
                ));
            }

            if (compareFunction.equals(wrong, reality)) {
                throw new AssertException(
                    "Did not expect " + printFunction.toString(wrong) +
                        ", but got " + printFunction.toString(reality) + "."
                );
            }
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
