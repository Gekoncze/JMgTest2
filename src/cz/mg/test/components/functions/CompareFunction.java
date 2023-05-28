package cz.mg.test.components.functions;

import cz.mg.annotations.classes.Component;
import cz.mg.annotations.requirement.Mandatory;

public @Component interface CompareFunction<T> {
    boolean equals(@Mandatory T a, @Mandatory T b);
}
