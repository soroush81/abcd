package ir.soodeh.mancala.services.exceptions;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class InvalidMoveException extends AbstractThrowableProblem {

    public InvalidMoveException(String message) {
        super(null,"invalid move", Status.BAD_REQUEST, message);
    }

    public InvalidMoveException(String message, int pitIdx) {
        super(null,"invalid move", Status.BAD_REQUEST,String.format ("%s: %d", message, pitIdx));
    }
}
