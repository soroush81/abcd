package ir.soodeh.mancala.services.exceptions;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

public class PitNotFoundProblem extends AbstractThrowableProblem {

      public PitNotFoundProblem(int pitId) {
        super ( null,"Not Found", Status.NOT_FOUND,String.format ("Could not find selected pit %d", pitId  ) );
    }
}
