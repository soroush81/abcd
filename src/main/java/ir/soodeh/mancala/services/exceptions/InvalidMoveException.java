package ir.soodeh.mancala.services.exceptions;

public class InvalidMoveException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public InvalidMoveException(String message) {
        super(message);
    }

    public InvalidMoveException(String message, int pitIdx) {
        super(String.format ("%s: %d", message, pitIdx));
    }
}
