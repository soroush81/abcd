package ir.soodeh.mancala.repositories;

import ir.soodeh.mancala.models.Game;
import ir.soodeh.mancala.services.GameService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {GameRepositoryImpl.class})
class GameRepositoryImplTest {
    @Autowired
    private GameRepositoryImpl gameRepository;

    @Test
    void create() {
    }

    @Test
    void findById() {
        Game createdGame = new Game ();
        this.gameRepository.create ( createdGame );
        Game foundGame = gameRepository.findById ( createdGame.getId () );
        Assert.assertEquals ( createdGame, foundGame );
    }
}