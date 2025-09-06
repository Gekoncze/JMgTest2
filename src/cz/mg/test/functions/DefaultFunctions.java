package cz.mg.test.functions;

import cz.mg.annotations.classes.Static;
import cz.mg.functions.EqualsFunction;

import java.util.Objects;

public @Static class DefaultFunctions {
    public static <T> EqualsFunction<T> EQUALS() {
        return (a, b) -> {
            if (a instanceof Number na && b instanceof Number nb) {
                return Numbers.equals(na, nb);
            } else {
                return Objects.equals(a, b);
            }
        };
    }
}
