package jwsa;

public enum RoutineType {
    DataSet(1), Scalar(2), NonQuery(3);

    private int value;

    private RoutineType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
