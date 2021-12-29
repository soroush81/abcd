package ir.soodeh.mancala.services.exceptions;

public class GameNotFoundException extends RuntimeException{
    public GameNotFoundException(int gameId) {
        super(String.format ("Could not find game %d", gameId));
    }
}
