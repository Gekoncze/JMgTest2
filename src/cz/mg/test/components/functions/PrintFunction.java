package cz.mg.test.components.functions;

import cz.mg.annotations.classes.Component;
import cz.mg.annotations.requirement.Mandatory;

public @Component interface PrintFunction<T> {
    String toString(@Mandatory T a);
}
