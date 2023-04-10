package cz.mg.test.functions;

import cz.mg.annotations.classes.Utility;
import cz.mg.annotations.requirement.Mandatory;

public @Utility interface PrintFunction<T> {
    String toString(@Mandatory T a);
}
