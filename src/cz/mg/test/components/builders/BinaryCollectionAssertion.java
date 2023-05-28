package cz.mg.test.components.builders;

import cz.mg.annotations.classes.Component;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.test.exceptions.AssertException;
import cz.mg.test.components.functions.CompareFunction;
import cz.mg.test.components.functions.PrintFunction;
import cz.mg.test.components.functions.common.DefaultCompareFunction;

import java.util.Iterator;
import java.util.Objects;

public @Component class BinaryCollectionAssertion<T> {
    private final @Optional Iterable<T> expectation;
    private final @Optional Iterable<T> reality;
    private @Mandatory CompareFunction<T> compareFunction = new DefaultCompareFunction<>();
    private @Mandatory PrintFunction<T> printFunction = Objects::toString;
    private @Optional String message;
    private @Optional Boolean verbose;
    private @Optional String verboseBegin;
    private @Optional String verboseDelimiter;
    private @Optional String verboseEnd;

    public BinaryCollectionAssertion(@Optional Iterable<T> expectation, @Optional Iterable<T> reality) {
        this.expectation = expectation;
        this.reality = reality;
    }

    public BinaryCollectionAssertion<T> withCompareFunction(@Mandatory CompareFunction<T> compareFunction) {
        this.compareFunction = compareFunction;
        return this;
    }

    public BinaryCollectionAssertion<T> withPrintFunction(@Mandatory PrintFunction<T> printFunction) {
        this.printFunction = printFunction;
        return this;
    }

    public BinaryCollectionAssertion<T> withMessage(@Mandatory String message) {
        this.message = message;
        return this;
    }

    public BinaryCollectionAssertion<T> verbose(
        @Mandatory String begin,
        @Mandatory String delimiter,
        @Mandatory String end
    ) {
        this.verbose = true;
        this.verboseBegin = begin;
        this.verboseDelimiter = delimiter;
        this.verboseEnd = end;
        return this;
    }

    public void areEqual() {
        assertEquals(expectation, reality, compareFunction, printFunction, this::buildMessage);
    }

    private @Mandatory String buildMessage(@Mandatory String error) {
        String message = this.message == null ? error : error + " " + this.message;
        if (Boolean.TRUE.equals(verbose)) {
            String expectationEntriesMessage = "";
            if (expectation != null) {
                expectationEntriesMessage = "\nExpectation: " + buildEntriesMessage(expectation);
            }

            String realityEntriesMessage = "";
            if (reality != null) {
                realityEntriesMessage = "\nReality: " + buildEntriesMessage(reality);
            }

            return message + expectationEntriesMessage + realityEntriesMessage;
        } else {
            return message;
        }
    }

    private @Mandatory String buildEntriesMessage(@Mandatory Iterable<T> collection) {
        StringBuilder builder = new StringBuilder();

        boolean first = true;
        for (T element : collection) {
            if (first) {
                first = false;
            } else {
                builder.append(verboseDelimiter);
            }

            if (element != null) {
                builder.append(verboseBegin);
                builder.append(printFunction.toString(element));
                builder.append(verboseEnd);
            } else {
                builder.append("null");
            }
        }

        return builder.toString();
    }

    private static <T> void assertEquals(
        @Optional Iterable<T> expectation,
        @Optional Iterable<T> reality,
        @Mandatory CompareFunction<T> compareFunction,
        @Mandatory PrintFunction<T> printFunction,
        @Mandatory MessageBuilder messageBuilder
    ) {
        if (expectation == reality) {
            return;
        }

        if (expectation == null) {
            throw new AssertException(messageBuilder.build("Expected collection to be null, but was not null."));
        }

        if (reality == null) {
            throw new AssertException(messageBuilder.build("Expected collection to not be null, but was null."));
        }

        int expectedCount = count(expectation);
        int actualCount = count(reality);

        if (expectedCount != actualCount) {
            throw new AssertException(messageBuilder.build(
                "Expected collection count to be " + expectedCount + ", but was " + actualCount + "."
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
                throw new AssertException(messageBuilder.build(
                    "Expected item at " + i + " to be null, but was " + printFunction.toString(actualItem) + "."
                ));
            }

            if (actualItem == null) {
                throw new AssertException(messageBuilder.build(
                    "Expected item at " + i + " to be " + printFunction.toString(expectedItem) + ", but was null."
                ));
            }

            if (!compareFunction.equals(expectedItem, actualItem)) {
                throw new AssertException(messageBuilder.build(
                    "Expected item at " + i + " to be " + printFunction.toString(expectedItem) + ", " +
                        "but was " + printFunction.toString(actualItem) + "."
                ));
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

    private interface MessageBuilder {
        @Mandatory String build(@Mandatory String error);
    }
}
