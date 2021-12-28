package ir.soodeh.mancala.exceptions;

public class InvalidMoveException extends RuntimeException {

    public InvalidMoveException(String message) {
        super(message);
    }

    public InvalidMoveException(String message, int pitIdx) {
        super(String.format ("%s: %d", message, pitIdx));
    }
}
