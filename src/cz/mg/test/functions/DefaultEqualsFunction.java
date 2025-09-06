package cz.mg.test.functions;

import cz.mg.annotations.classes.Component;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.functions.EqualsFunction;

import java.util.Objects;

public @Component class DefaultEqualsFunction<T> implements EqualsFunction<T> {
    private final NumberEqualsFunction numberEqualsFunction = new NumberEqualsFunction();

    @Override
    public boolean equals(@Mandatory Object a, @Mandatory Object b) {
        if (a instanceof Number && b instanceof Number) {
            return numberEqualsFunction.equals((Number) a, (Number) b);
        } else {
            return Objects.equals(a, b);
        }
    }
}
