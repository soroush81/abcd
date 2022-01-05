package ir.soodeh.mancala.services.exceptions;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class GameNotFoundProblem extends AbstractThrowableProblem {
    public GameNotFoundProblem(String gameId) {
        super(null,"Not Found", Status.NOT_FOUND, String.format ("Could not find game %s", gameId));
    }
}
