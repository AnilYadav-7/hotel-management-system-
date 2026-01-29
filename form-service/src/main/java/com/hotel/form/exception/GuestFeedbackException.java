package com.hotel.form.exception;

public class GuestFeedbackException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private String errorCode;

    public GuestFeedbackException() {
        super();
        this.errorCode = "FEEDBACK_ERROR";
    }

    public GuestFeedbackException(String message) {
        super(message);
        this.errorCode = "FEEDBACK_ERROR";
    }

    public GuestFeedbackException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public GuestFeedbackException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "FEEDBACK_ERROR";
    }

    public GuestFeedbackException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
