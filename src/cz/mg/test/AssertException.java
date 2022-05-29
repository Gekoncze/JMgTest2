package cz.mg.test;

import cz.mg.annotations.requirement.Mandatory;

public class AssertException extends RuntimeException {
    public AssertException(@Mandatory String message) {
        super(message);
    }

    public AssertException(@Mandatory String message, @Mandatory Exception cause) {
        super(message, cause);
    }
}
