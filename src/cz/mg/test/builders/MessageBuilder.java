package cz.mg.test.builders;

import cz.mg.annotations.classes.Component;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;

@Component class MessageBuilder {
    private final @Mandatory StringBuilder builder = new StringBuilder();

    public MessageBuilder() {
    }

    public @Mandatory MessageBuilder addMessage(@Optional String message) {
        if (message != null && !message.isBlank()) {
            builder.append('\n');
            builder.append(message);
        }
        return this;
    }

    public @Mandatory String build() {
        return builder.toString();
    }
}
