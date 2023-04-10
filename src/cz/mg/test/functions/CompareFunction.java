package cz.mg.test.functions;

import cz.mg.annotations.classes.Utility;
import cz.mg.annotations.requirement.Mandatory;

public @Utility interface CompareFunction<T> {
    boolean equals(@Mandatory T a, @Mandatory T b);
}
