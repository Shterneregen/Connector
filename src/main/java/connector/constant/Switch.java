package connector.constant;

public enum Switch {
    ON("on"),
    OFF("off");

    private String mode;

    Switch(String mode) {
        this.mode = mode;
    }

    public String getMode() {
        return mode;
    }

}
