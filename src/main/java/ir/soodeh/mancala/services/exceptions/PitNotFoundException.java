package ir.soodeh.mancala.services.exceptions;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class PitNotFoundException extends AbstractThrowableProblem {

    public PitNotFoundException(int pitId) {
        super ( null,"invalid move", Status.NOT_FOUND,String.format ("Selected Pit id Invalid %d", pitId  ) );
    }
}
