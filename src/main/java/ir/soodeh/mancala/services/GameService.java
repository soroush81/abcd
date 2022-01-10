package ir.soodeh.mancala.services;

import ir.soodeh.mancala.model.Game;

import java.util.Optional;

public interface GameService {

    /**
     * create a new game
     * @return the new game
     */
    Optional<Game> createGame();

    /**
     * play one round of game
     * @param gameId
     * @param pitIdx the id of the pit that has been selected
     * @return the game object of the given id after play the round
     */
    Optional<Game> play(final String gameId, final int pitIdx);

    /**
     * reset that game
     * @param gameId id of the game need to be reset
     */
    void resetGame(final String gameId);


}
