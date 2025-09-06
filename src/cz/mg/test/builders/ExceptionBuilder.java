package cz.mg.test.builders;

import cz.mg.annotations.classes.Component;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.test.exceptions.AssertException;

@Component class ExceptionBuilder {
    private final @Mandatory MessageBuilder messageBuilder = new MessageBuilder();
    private @Optional Exception cause;

    public ExceptionBuilder() {
    }

    public @Mandatory ExceptionBuilder addMessage(@Optional String message) {
        messageBuilder.addMessage(message);
        return this;
    }

    public @Mandatory ExceptionBuilder addMessages(@Optional String... messages) {
        if (messages != null) {
            for (String message : messages) {
                addMessage(message);
            }
        }
        return this;
    }

    public @Mandatory ExceptionBuilder withCause(@Optional Exception cause) {
        this.cause = cause;
        return this;
    }

    public @Mandatory AssertException build() {
        return new AssertException(messageBuilder.build(), cause);
    }
}
