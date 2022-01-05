package ir.soodeh.mancala.services.exceptions;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class InvalidMoveProblem extends AbstractThrowableProblem {

    public InvalidMoveProblem(String message) {
        super(null,"invalid move", Status.BAD_REQUEST, message);
    }

    public InvalidMoveProblem(String message, int pitIdx) {
        super(null,"invalid move", Status.BAD_REQUEST,String.format ("%s: %d", message, pitIdx));
    }
}
