package jwsa;

public enum CompressionType {
    NONE(0),
    GZip(2);

    final int value;

    CompressionType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
