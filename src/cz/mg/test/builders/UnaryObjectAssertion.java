package cz.mg.test.builders;

import cz.mg.annotations.classes.Utility;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.test.AssertException;
import cz.mg.test.functions.PrintFunction;

import java.util.Objects;

public @Utility class UnaryObjectAssertion<T> {
    private final @Optional T object;
    private @Mandatory PrintFunction<T> printFunction = Objects::toString;
    private @Optional String message;

    public UnaryObjectAssertion(@Optional T object) {
        this.object = object;
    }

    public UnaryObjectAssertion<T> withPrintFunction(@Mandatory PrintFunction<T> printFunction) {
        this.printFunction = printFunction;
        return this;
    }

    public UnaryObjectAssertion<T> withMessage(@Mandatory String message) {
        this.message = message;
        return this;
    }

    public void isNull() {
        if (object != null) {
            throw new AssertException(extendMessage(
                "Expected null, but got " + printFunction.toString(object) + ".",
                message
            ));
        }
    }

    public void isNotNull() {
        if (object == null) {
            throw new AssertException(extendMessage(
                "Unexpected null value.",
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
