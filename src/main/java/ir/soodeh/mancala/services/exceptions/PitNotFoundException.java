package ir.soodeh.mancala.services.exceptions;

public class PitNotFoundException extends RuntimeException {
    public PitNotFoundException(int pitId) {
        super (String.format ("Could not find selected pit %d", pitId  ) );
    }

}
