package cz.mg.test.builders;

import cz.mg.annotations.classes.Utility;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.test.AssertException;
import cz.mg.test.functions.CompareFunction;
import cz.mg.test.functions.PrintFunction;
import cz.mg.test.functions.common.DefaultCompareFunction;

import java.util.Iterator;
import java.util.Objects;

public @Utility class CollectionAssertion<T> {
    private final @Optional Iterable<T> expectation;
    private final @Optional Iterable<T> reality;
    private @Mandatory CompareFunction<T> compareFunction = new DefaultCompareFunction<>();
    private @Mandatory PrintFunction<T> printFunction = Objects::toString;
    private @Optional String message;

    public CollectionAssertion(@Optional Iterable<T> expectation, @Optional Iterable<T> reality) {
        this.expectation = expectation;
        this.reality = reality;
    }


    public CollectionAssertion<T> withCompareFunction(@Mandatory CompareFunction<T> compareFunction) {
        this.compareFunction = compareFunction;
        return this;
    }

    public CollectionAssertion<T> withPrintFunction(@Mandatory PrintFunction<T> printFunction) {
        this.printFunction = printFunction;
        return this;
    }

    public CollectionAssertion<T> withMessage(@Mandatory String message) {
        this.message = message;
        return this;
    }

    public void areEqual() {
        assertEquals(expectation, reality, compareFunction, printFunction, message);
    }

    private static <T> void assertEquals(
        @Optional Iterable<T> expectation,
        @Optional Iterable<T> reality,
        @Mandatory CompareFunction<T> compareFunction,
        @Mandatory PrintFunction<T> printFunction,
        @Optional String message
    ) {
        if (expectation == reality) {
            return;
        }

        if (expectation == null) {
            throw new AssertException(extendMessage("Expected collection to be null, but was not null.", message));
        }

        if (reality == null) {
            throw new AssertException(extendMessage("Expected collection to not be null, but was null.", message));
        }

        int expectedCount = count(expectation);
        int actualCount = count(reality);

        if (expectedCount != actualCount) {
            throw new AssertException(extendMessage(
                "Expected collection count to be " + expectedCount + ", but was " + actualCount + ".",
                message
            ));
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
                throw new AssertException(extendMessage(
                    "Expected item at " + i + " to be null, but was " + printFunction.toString(actualItem) + ".",
                    message
                ));
            }

            if (actualItem == null) {
                throw new AssertException(extendMessage(
                    "Expected item at " + i + " to be " + printFunction.toString(expectedItem) + ", but was null.",
                    message
                ));
            }

            if (!compareFunction.equals(expectedItem, actualItem)) {
                throw new AssertException(
                    "Expected item at " + i + " to be " + printFunction.toString(expectedItem) + ", " +
                        "but was " + printFunction.toString(actualItem) + "."
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

    private static @Mandatory String extendMessage(@Mandatory String mandatoryPart, @Optional String optionalPart) {
        if (optionalPart == null) {
            return mandatoryPart;
        } else {
            return mandatoryPart + " " + optionalPart;
        }
    }
}
