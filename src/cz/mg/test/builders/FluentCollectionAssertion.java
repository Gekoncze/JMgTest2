package cz.mg.test.builders;

import cz.mg.annotations.classes.Component;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.functions.EqualsFunction;
import cz.mg.functions.EqualsFunctions;
import cz.mg.functions.FormatFunction;
import cz.mg.functions.FormatFunctions;
import cz.mg.test.exceptions.AssertException;

import java.util.Iterator;

public @Component class FluentCollectionAssertion<T> {
    private final @Optional Iterable<T> reality;
    private @Optional String message;
    private @Mandatory EqualsFunction<T> equalsFunction = EqualsFunctions.EQUALS();
    private @Mandatory FormatFunction<T> formatFunction = FormatFunctions.TO_STRING();
    private @Optional Details details;

    public FluentCollectionAssertion(@Optional Iterable<T> reality) {
        this.reality = reality;
    }

    public @Mandatory FluentCollectionAssertion<T> withMessage(@Mandatory String message) {
        this.message = message;
        return this;
    }

    public @Mandatory FluentCollectionAssertion<T> withEqualsFunction(@Mandatory EqualsFunction<T> equalsFunction) {
        this.equalsFunction = equalsFunction;
        return this;
    }

    public @Mandatory FluentCollectionAssertion<T> withFormatFunction(@Mandatory FormatFunction<T> formatFunction) {
        this.formatFunction = formatFunction;
        return this;
    }

    public @Mandatory FluentCollectionAssertion<T> withDetails(@Mandatory String delimiter) {
        return this.withDetails("", delimiter, "");
    }

    public @Mandatory FluentCollectionAssertion<T> withDetails(
        @Mandatory String prefix,
        @Mandatory String delimiter,
        @Mandatory String postfix
    ) {
        this.details = new Details(prefix, delimiter, postfix);
        return this;
    }

    public void isEqualTo(@Optional Iterable<T> expectation) {
        Iterable<T> reality = this.reality;

        if (expectation == reality) {
            return;
        }

        if (expectation == null) {
            throw createException(expectation, "Expected collection to be null, but was not null.");
        }

        if (reality == null) {
            throw createException(expectation, "Expected collection to not be null, but was null.");
        }

        int expectedCount = count(expectation);
        int actualCount = count(reality);
        @Optional String countMessage = expectedCount != actualCount
            ? "Expected collection count to be " + expectedCount + ", but was " + actualCount + "."
            : null;

        int i = -1;
        Iterator<T> expectationIterator = expectation.iterator();
        Iterator<T> realityIterator = reality.iterator();
        while (expectationIterator.hasNext() && realityIterator.hasNext()) {
            i++;
            @Optional T expectedObject = expectationIterator.next();
            @Optional T actualObject = realityIterator.next();

            if (!equalsFunction.equalsOptional(expectedObject, actualObject)) {
                throw createException(
                    expectation,
                    "Expected item at " + i + " to be " + formatFunction.formatOptional(expectedObject) + ", " +
                        "but was " + formatFunction.formatOptional(actualObject) + "."
                );
            }
        }

        if (countMessage != null) {
            throw createException(expectation, countMessage);
        }
    }

    public void isNotEqualTo(@Optional Iterable<T> expectation) {
        Iterable<T> reality = this.reality;

        if (expectation == reality) {
            throw createException(expectation, "Expected collections to not be equal.");
        }

        if (expectation == null) {
            return;
        }

        if (reality == null) {
            return;
        }

        int expectedCount = count(expectation);
        int actualCount = count(reality);
        if (expectedCount != actualCount) {
            return;
        }

        int i = -1;
        Iterator<T> expectationIterator = expectation.iterator();
        Iterator<T> realityIterator = reality.iterator();
        while (expectationIterator.hasNext() && realityIterator.hasNext()) {
            i++;
            @Optional T expectedObject = expectationIterator.next();
            @Optional T actualObject = realityIterator.next();

            if (!equalsFunction.equalsOptional(expectedObject, actualObject)) {
                return;
            }
        }

        throw createException(expectation, "Expected collections to not be equal.");
    }

    private int count(@Mandatory Iterable<T> iterable) {
        int count = 0;
        for (T ignored : iterable) {
            count++;
        }
        return count;
    }

    private @Optional String createExpectationDetailMessage(@Optional Iterable<T> expectation) {
        return createLabeledDetailMessage(expectation, "Expectation");
    }

    private @Optional String createRealityDetailMessage() {
        return createLabeledDetailMessage(reality, "Reality");
    }

    private @Optional String createLabeledDetailMessage(
        @Optional Iterable<T> collection,
        @Mandatory String label
    ) {
        return collection != null && details != null
            ? "\n" + label + ": " + createDetailMessage(collection, details)
            : null;
    }

    private @Mandatory String createDetailMessage(@Mandatory Iterable<T> collection, @Mandatory Details details) {
        StringBuilder builder = new StringBuilder();

        boolean first = true;
        for (T element : collection) {
            if (first) {
                first = false;
            } else {
                builder.append(details.delimiter);
            }

            if (element != null) {
                builder.append(details.prefix);
                builder.append(formatFunction.format(element));
                builder.append(details.postfix);
            } else {
                builder.append("null");
            }
        }

        return builder.toString();
    }

    private @Mandatory AssertException createException(
        @Optional Iterable<T> expectation,
        @Optional String... specificMessages
    ) {
        return new ExceptionBuilder()
            .addMessage(message)
            .addMessages(specificMessages)
            .addMessage(createExpectationDetailMessage(expectation))
            .addMessage(createRealityDetailMessage())
            .build();
    }

    private record Details(
        @Optional String prefix,
        @Optional String delimiter,
        @Optional String postfix
    ) {}
}
