package ir.soodeh.mancala.repositories;

import ir.soodeh.mancala.domain.Board;
import ir.soodeh.mancala.domain.Game;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

class GameRepositoryImplTest {
    private GameRepositoryImpl gameRepository;

    @BeforeEach
    void setUp() {
        this.gameRepository = new GameRepositoryImpl ();
    }

    @Test
    void findById() {
        Game createdGame = new Game ();
        this.gameRepository.save ( createdGame );
        Optional<Game> foundGame = gameRepository.findById ( createdGame.getId () );
        Assert.assertEquals ( createdGame, foundGame.get () );
    }
}