package cz.mg.test.exceptions;

import cz.mg.annotations.classes.Error;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;

public @Error class AssertException extends RuntimeException {
    public AssertException(@Mandatory String message) {
        super(message);
    }

    public AssertException(@Mandatory String message, @Optional Exception cause) {
        super(message, cause);
    }
}
