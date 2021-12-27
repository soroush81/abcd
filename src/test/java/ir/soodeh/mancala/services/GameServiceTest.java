package ir.soodeh.mancala.services;

import ir.soodeh.mancala.models.Game;
import ir.soodeh.mancala.enums.Player;
import ir.soodeh.mancala.repositories.GameRepositoryImpl;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {GameService.class, GameRepositoryImpl.class})
class GameServiceTest {

    @Autowired
    public GameService gameService;

    @Autowired
    public GameRepositoryImpl gameRepository;

    @Before
    public void init(){
        Game game1 = new Game();
        game1.getBoard ().getPit ( Player.PLAYER_1.getCalaIdx () ).setStoneCount ( 30 );
        game1.getBoard ().getPit ( Player.PLAYER_2.getCalaIdx () ).setStoneCount ( 42 );
    }

    @Test
    void createGame() {
    }

    @Test
    void play_basic() {
        Game mockedGame = mock(Game.class);
        when(gameRepository.findById ( 1 )).thenReturn ( Optional.of(mockedGame) );

        Game game = this.gameService.play ( 1,3 );
        assertThat(game).isEqualTo(mockedGame);
    }

    @Test
    void resetGame() {
    }
}