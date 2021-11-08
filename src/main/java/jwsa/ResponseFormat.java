package jwsa;

public enum ResponseFormat {
    XML(1),
    JSON(2);

    final int value;

    ResponseFormat(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
