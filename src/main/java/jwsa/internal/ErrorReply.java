package jwsa.internal;

public  class ErrorReply
{
    private Error error;

    public ErrorReply(Error error) {
        this.error = error;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }
}


