package ir.soodeh.mancala.services;

import ir.soodeh.mancala.exceptions.InvalidMoveException;
import ir.soodeh.mancala.models.Board;
import ir.soodeh.mancala.models.Game;
import ir.soodeh.mancala.enums.Player;
import ir.soodeh.mancala.models.Pit;
import ir.soodeh.mancala.repositories.GameRepository;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {GameService.class})
class GameServiceTest {

    @Autowired
    public GameService gameService;

    @MockBean
    public GameRepository gameRepository;

    private Game playingGame;
    private Game lastRoundGame;
    private Board board;

    @BeforeEach
    public void init(){
        playingGame = new Game();
        lastRoundGame = new Game();
        board = new Board();
        lastRoundGame.getBoard ().getPits ().stream ().filter(pit -> !pit.isCala()).forEach ( pit -> pit.setStoneCount ( 0 ) );
        lastRoundGame.getBoard ().getPit ( Player.PLAYER_1.getCalaIdx () ).setStoneCount ( 23 );
        lastRoundGame.getBoard ().getPit ( Player.PLAYER_2.getCalaIdx () ).setStoneCount ( 40 );
        lastRoundGame.getBoard ().getPit(13).setStoneCount ( 2 );
        lastRoundGame.getBoard ().getPit ( 3 ).setStoneCount ( 5 );
        lastRoundGame.getBoard ().getPit ( 6 ).setStoneCount ( 2 );
        lastRoundGame.setCurrentPlayer ( Player.PLAYER_2 );
    }

    @Test
    @DisplayName("create game")
    void createGame() {
        Game mockedGame = new Game();
        when(gameRepository.create (any(Game.class))).thenReturn (mockedGame);
        Game createdGame = gameService.createGame ();
        Assert.assertNotNull (createdGame);
        Assert.assertEquals (mockedGame, createdGame );
    }

    @Test
    @DisplayName("play one round of game")
    void play_basic() {
         Game mockedGame = new Game();
         when(gameRepository.findById ( 1000 )).thenReturn (mockedGame);

        Game game = this.gameService.play ( 1000,3 );
        assertThat(game).isEqualTo(mockedGame);
        verify ( this.gameRepository, times(1))
                .findById(1000);
    }

    @Test
    @DisplayName("play last round of the game")
    void play_lastRound(){
        when(this.gameRepository.findById ( 1003 )).thenReturn (lastRoundGame);
        lastRoundGame = this.gameService.play ( 1003,13 );
        Assert.assertEquals ( 31,lastRoundGame.getBoard ().getPitsStoneCount (Player.PLAYER_1 ) + lastRoundGame.getBoard ().getCalaStoneCount ( Player.PLAYER_1 ));
        Assert.assertEquals ( 41,lastRoundGame.getBoard ().getPitsStoneCount ( Player.PLAYER_2 ) + lastRoundGame.getBoard ().getCalaStoneCount ( Player.PLAYER_2 ));
        Assert.assertEquals (Player.PLAYER_2, lastRoundGame.getWinner ());
    }


    @Test
    @DisplayName("check reset game")
    void resetGame() {
        Game mockedGame = new Game();
        when(gameRepository.findById ( 1000 )).thenReturn (mockedGame);
        gameService.resetGame ( mockedGame );
        Assert.assertEquals ( 0, mockedGame.getBoard ( ).getPitsStoneCount ( Player.PLAYER_1 ));
        Assert.assertEquals ( 0,mockedGame.getBoard ( ).getPitsStoneCount ( Player.PLAYER_2 ));
    }

    @Test
    @DisplayName("if player selected empty pit")
    void play_emptyPit() {
        Game mockedGame = new Game();
        mockedGame.getBoard ().getPit ( 3 ).setStoneCount ( 0 );
        when(gameRepository.create (any(Game.class))).thenReturn (mockedGame);
        when(this.board.getPit(3))
                .thenReturn(new Pit (3,0, false));

        assertThatThrownBy(() -> this.gameService.play ( 1000,3 ))
                .isInstanceOf( InvalidMoveException.class)
                .hasMessage("The Pit is empty,3");
//        //negative values
//        assertThrows(InvalidMoveException.class, () -> {
//            gameService.play (1000, 3);
//        });
    }

}