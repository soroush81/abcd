package ir.soodeh.mancala.repositories;

import ir.soodeh.mancala.domain.Game;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {GameRepositoryImpl.class})
class GameRepositoryImplTest {
    @Autowired
    private GameRepositoryImpl gameRepository;

    @BeforeEach
    void setUp() {
        this.gameRepository = new GameRepositoryImpl ();
    }


    @Test
    @DirtiesContext(classMode= DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    void findById() {
        Game createdGame = new Game ();
        this.gameRepository.create ( createdGame );
        Optional<Game> foundGame = gameRepository.findById ( createdGame.getId () );
        Assert.assertEquals ( createdGame, foundGame.get () );
    }
}