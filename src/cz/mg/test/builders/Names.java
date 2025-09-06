package cz.mg.test.builders;

import cz.mg.annotations.classes.Static;
import cz.mg.annotations.requirement.Mandatory;

@Static class Names {
    public static @Mandatory String[] get(@Mandatory Class<?> first, @Mandatory Class<?> second) {
        String firstName = first.getSimpleName();
        String secondName = second.getSimpleName();

        if (firstName.equals(secondName)) {
            firstName = first.getName();
            secondName = second.getName();
        }

        return new String[] {
            firstName,
            secondName
        };
    }
}
