package jwsa;

public enum HttpMethod {
    GET(1),
    POST(2);

    final int value;

    HttpMethod(int value) {
        this.value = value;
    }
}

