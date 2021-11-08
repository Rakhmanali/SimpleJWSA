package jwsa;

public enum ResponseFormat {
    XML(1),
    JSON(2);

    private int value;

    private ResponseFormat(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
