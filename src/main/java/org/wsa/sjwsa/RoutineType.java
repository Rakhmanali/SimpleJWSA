package org.wsa.sjwsa;

public enum RoutineType {
    DataSet(1), Scalar(2), NonQuery(3);

    final int value;

    RoutineType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
