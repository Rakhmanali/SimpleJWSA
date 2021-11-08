package jwsa.internal;

public class Error {

    private String function;
    private String code;
    private String message;

    public Error(String function, String code, String message) {
        this.function = function;
        this.code = code;
        this.message = message;
    }

    public String getFunction() {
        return function;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
