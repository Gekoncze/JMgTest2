package cz.mg.test.functions.common;

import cz.mg.annotations.classes.Utility;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.test.functions.CompareFunction;

import java.util.Objects;

public @Utility class DefaultCompareFunction<T> implements CompareFunction<T> {
    private final NumberCompareFunction numberCompareFunction = new NumberCompareFunction();

    @Override
    public boolean equals(@Mandatory Object a, @Mandatory Object b) {
        if (a instanceof Number && b instanceof Number) {
            return numberCompareFunction.equals((Number) a, (Number) b);
        } else {
            return Objects.equals(a, b);
        }
    }
}
