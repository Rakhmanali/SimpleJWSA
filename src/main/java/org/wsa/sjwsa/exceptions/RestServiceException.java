package org.wsa.sjwsa.exceptions;

public class RestServiceException extends Exception {
    private final String code;
    private final String originalMessage;

    public RestServiceException(String message, String code, String originalMessage) {
        super(message);
        this.code = code;
        this.originalMessage = originalMessage;
    }

    public String getOriginalMessage() {
        return this.originalMessage;
    }

    public String getCode() {
        return this.code;
    }

    public static boolean IsSessionEmptyOrExpired(Exception ex) {
        if (ex instanceof RestServiceException) {
            RestServiceException restServiceException = (RestServiceException) ex;
            return restServiceException.code.equalsIgnoreCase("MI008") == true ||
                    restServiceException.code.equalsIgnoreCase("MI005") == true;
        }

        return false;
    }
}

