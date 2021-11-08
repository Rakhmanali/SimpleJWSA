package jwsa;

public enum CompressionType {
    NONE(0),
    GZip(2);

    private int value;

    private CompressionType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
