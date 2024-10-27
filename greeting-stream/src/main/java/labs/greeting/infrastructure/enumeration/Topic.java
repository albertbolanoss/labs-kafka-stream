package labs.greeting.infrastructure.enumeration;

import lombok.Getter;

@Getter
public enum Topic {
    GREETINGS("greetings"),
    GREETINGS_UPPERCASE("uppercase"),
    GREETINGS_SPANISH("greetings_spanish");

    private final String name;

    Topic(String name) {
        this.name = name;
    }
}
