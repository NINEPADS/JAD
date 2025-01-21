package enums;

public enum State {

    WAIT(1, "wait"), RUN(2, "run"), RELAX(3, "end");
    private final int value;
    private final String description;
    State(int value, String description) {
        this.value = value;
        this.description = description;
    }
    public int getValue() {return value;}
    public String getDescription() {return description;}
}
