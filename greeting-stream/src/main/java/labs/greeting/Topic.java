package labs.greeting;

import lombok.Getter;

@Getter
public enum Topic {
    GREETINGS("greetings"),
    GREETINGS_UPPERCASE("uppercase");

    private final String name;

    Topic(String name) {
        this.name = name;
    }
}
