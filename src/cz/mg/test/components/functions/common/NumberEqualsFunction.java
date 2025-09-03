package cz.mg.test.components.functions.common;

import cz.mg.annotations.classes.Component;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.functions.EqualsFunction;

import java.util.Objects;

public @Component class NumberEqualsFunction implements EqualsFunction<Number> {
    @Override
    public boolean equals(@Mandatory Number a, @Mandatory Number b) {
        if (areIntegers(a, b)) {
            return convert(a) == convert(b);
        } else {
            return Objects.equals(a, b);
        }
    }

    private static boolean areIntegers(@Mandatory Number expectation, @Mandatory Number reality) {
        return isInteger(expectation) && isInteger(reality);
    }

    private static boolean isInteger(@Mandatory Number number) {
        return number instanceof Long
            || number instanceof Integer
            || number instanceof Short
            || number instanceof Byte;
    }

    private static long convert(@Mandatory Number number) {
        if (number instanceof Long) {
            return (long) number;
        }

        if (number instanceof Integer) {
            return (int) number;
        }

        if (number instanceof Short) {
            return (short) number;
        }

        if (number instanceof Byte) {
            return (byte) number;
        }

        throw new UnsupportedOperationException(
            "Unsupported number type " + number.getClass().getSimpleName() + "."
        );
    }
}
