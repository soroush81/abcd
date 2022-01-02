package ir.soodeh.mancala.services.exceptions;

public class GameNotFoundException extends RuntimeException{
    public GameNotFoundException(Integer gameId) {
        super(String.format ("Could not find game %d", gameId));
    }
}
