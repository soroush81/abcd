package ir.soodeh.mancala.services.exceptions;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

public class PitNotFoundException extends AbstractThrowableProblem {

      public PitNotFoundException(int pitId) {
        super ( null,"Invalid Pit", Status.NOT_FOUND,String.format ("Could not find selected pit %d", pitId  ) );
    }
}
