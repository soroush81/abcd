package ir.soodeh.mancala.services.exceptions;

public class GameNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public GameNotFoundException(String gameId) {
//        super(msg);
        super(String.format ("Could not find game %s", gameId));
    }
}
