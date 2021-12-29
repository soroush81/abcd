package ir.soodeh.mancala.services.exceptions;

public class PitNotFoundException extends RuntimeException {

    public PitNotFoundException(int pitId) {
        super ( String.format ("Selected Pit id Invalid %d", pitId  ) );
    }
}
