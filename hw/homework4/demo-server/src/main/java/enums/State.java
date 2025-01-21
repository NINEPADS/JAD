package enums;

public enum State {
    WAIT(0, "wait"), RUN(1, "run"), RELAX(2, "relax");
    private final int value;
    private final String description;
    State(int value, String description) {
        this.value = value;
        this.description = description;
    }
    public int getValue() {return value;}
    public String getDescription() {return description;}
}
