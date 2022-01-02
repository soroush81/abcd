package ir.soodeh.mancala.services.exceptions;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class GameNotFoundException extends AbstractThrowableProblem {
    public GameNotFoundException(Integer gameId) {
        super(null,"Invalid game", Status.NOT_FOUND, String.format ("Could not find game %d", gameId));
    }
}
