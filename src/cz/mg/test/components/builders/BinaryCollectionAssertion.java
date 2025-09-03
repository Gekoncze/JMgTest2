package cz.mg.test.components.builders;

import cz.mg.annotations.classes.Component;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.functions.EqualsFunction;
import cz.mg.functions.FormatFunction;
import cz.mg.functions.FormatFunctions;
import cz.mg.test.components.functions.common.DefaultEqualsFunction;
import cz.mg.test.exceptions.AssertException;

import java.util.Iterator;

public @Component class BinaryCollectionAssertion<T> {
    private final @Optional Iterable<T> expectation;
    private final @Optional Iterable<T> reality;
    private @Mandatory EqualsFunction<T> equalsFunction = new DefaultEqualsFunction<>();
    private @Mandatory FormatFunction<T> formatFunction = FormatFunctions.TO_STRING();
    private @Optional String message;
    private @Optional Boolean verbose;
    private @Optional String verboseBegin;
    private @Optional String verboseDelimiter;
    private @Optional String verboseEnd;

    public BinaryCollectionAssertion(@Optional Iterable<T> expectation, @Optional Iterable<T> reality) {
        this.expectation = expectation;
        this.reality = reality;
    }

    public BinaryCollectionAssertion<T> withCompareFunction(@Mandatory EqualsFunction<T> equalsFunction) {
        this.equalsFunction = equalsFunction;
        return this;
    }

    public BinaryCollectionAssertion<T> withPrintFunction(@Mandatory FormatFunction<T> formatFunction) {
        this.formatFunction = formatFunction;
        return this;
    }

    public BinaryCollectionAssertion<T> withMessage(@Mandatory String message) {
        this.message = message;
        return this;
    }

    public BinaryCollectionAssertion<T> verbose(@Mandatory String delimiter) {
        this.verbose = true;
        this.verboseBegin = "";
        this.verboseDelimiter = delimiter;
        this.verboseEnd = "";
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
        assertEquals(expectation, reality, equalsFunction, formatFunction, this::buildMessage);
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
                builder.append(formatFunction.format(element));
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
        @Mandatory EqualsFunction<T> equalsFunction,
        @Mandatory FormatFunction<T> formatFunction,
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
        String countMismatch = expectedCount != actualCount
            ? "Expected collection count to be " + expectedCount + ", but was " + actualCount + "."
            : null;

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
                    getCountMessage(countMismatch) +
                    "Expected item at " + i + " to be null, but was " + formatFunction.format(actualItem) + "."
                ));
            }

            if (actualItem == null) {
                throw new AssertException(messageBuilder.build(
                    getCountMessage(countMismatch) +
                    "Expected item at " + i + " to be " + formatFunction.format(expectedItem) + ", but was null."
                ));
            }

            if (!equalsFunction.equals(expectedItem, actualItem)) {
                throw new AssertException(messageBuilder.build(
                    getCountMessage(countMismatch) +
                    "Expected item at " + i + " to be " + formatFunction.format(expectedItem) + ", " +
                        "but was " + formatFunction.format(actualItem) + "."
                ));
            }
        }

        if (countMismatch != null) {
            throw new AssertException(messageBuilder.build(countMismatch));
        }
    }

    private static @Mandatory String getCountMessage(@Optional String message) {
        return message == null ? "" : message + "\n";
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
