package labs.greeting;

public enum Topic {
    GREETINGS("greetings"),
    GREETINGS_UPPERCASE("uppercase"),
    GREETINGS_SPANISH("greetings_spanish");

    private final String name;

    Topic(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
