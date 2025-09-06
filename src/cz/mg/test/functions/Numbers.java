package cz.mg.test.functions;

import cz.mg.annotations.classes.Static;
import cz.mg.annotations.requirement.Mandatory;

import java.util.Objects;

@Static class Numbers {
    public static boolean equals(@Mandatory Number a, @Mandatory Number b) {
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
        return switch (number) {
            case Long l -> l;
            case Integer i -> i;
            case Short s -> s;
            case Byte b -> b;
            default -> throw new UnsupportedOperationException(
                "Unsupported number of type " + number.getClass().getSimpleName() + "."
            );
        };
    }
}
