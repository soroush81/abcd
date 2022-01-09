package ir.soodeh.mancala.services.exceptions;

import java.util.Date;

public class ErrorMessage {
    private int statusCode;
    private Date timestamp;
    private String message;
    private String detail;

    public ErrorMessage(int statusCode, Date timestamp, String message, String detail) {
        this.statusCode = statusCode;
        this.timestamp = timestamp;
        this.message = message;
        this.detail = detail;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getDetail() {
        return detail;
    }
}