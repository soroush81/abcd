package ir.soodeh.mancala.services;

import ir.soodeh.mancala.domain.Game;

public interface GameService {

    /**
     * create a new game
     * @return the new game
     */
    Game createGame();

    /**
     * play one round of game
     * @param gameId
     * @param pitIdx the id of the pit that has been selected
     * @return the game object of the given id after play the round
     */
    Game play(final String gameId, final int pitIdx);

    /**
     * reset that game
     * @param gameId id of the game need to be reset
     */
    void resetGame(final String gameId);


}
