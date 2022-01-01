package ir.soodeh.mancala.services;

import ir.soodeh.mancala.domain.Pit;
import ir.soodeh.mancala.services.exceptions.InvalidMoveException;
import ir.soodeh.mancala.domain.Board;
import ir.soodeh.mancala.domain.Game;
import ir.soodeh.mancala.domain.Player;
import ir.soodeh.mancala.repositories.GameRepository;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {GameService.class})
class GameServiceTest {

    @Autowired
    private GameService gameService;

    @MockBean
    private GameRepository gameRepository;

    private Game game;
    private Board board;

    @BeforeEach
    public void setup(){
        game = new Game();
        this.game = Mockito.spy(game);
        board = new Board();
        this.board = Mockito.spy(game.getBoard ());
    }

    @Test
    @DisplayName("create game")
    void createGame() {
        when(gameRepository.create (any(Game.class))).thenReturn (game);
        Game createdGame = gameService.createGame ();
        Assert.assertNotNull (createdGame);
        Assert.assertEquals (game, createdGame );
    }

    @Test
    @DisplayName("play one round of game")
    void play_basic() {
         when(gameRepository.findById ( 1000 )).thenReturn (Optional.of(game));

        Game playedGame = this.gameService.play ( 1000,3 );
        assertThat(game).isEqualTo(playedGame);
        verify ( this.gameRepository, times(1))
                .findById(1000);
    }

    @Test
    @DisplayName("play last round of the game with winner")
    void play_lastRoundWithWinner(){
        game.getBoard ().getPits ().stream ().filter(pit -> !pit.isCala()).forEach ( pit -> pit.setStoneCount ( 0 ) );

        game.getBoard ().getPit ( Player.PLAYER_1.getCalaIdx () ).setStoneCount ( 23 );
        game.getBoard ().getPit ( Player.PLAYER_2.getCalaIdx () ).setStoneCount ( 40 );
        game.getBoard ().getPit(13).setStoneCount ( 2 );
        game.getBoard ().getPit ( 3 ).setStoneCount ( 5 );
        game.getBoard ().getPit ( 6 ).setStoneCount ( 2 );

        game.setCurrentPlayer ( Player.PLAYER_2 );

        when(this.gameRepository.findById ( 1002 )).thenReturn ( Optional.of(game));
        game = this.gameService.play ( 1002,13 );
        Assert.assertEquals ( 31,game.getBoard ().getPitsStoneCount (Player.PLAYER_1 ) + game.getBoard ().getCalaStoneCount ( Player.PLAYER_1 ));
        Assert.assertEquals ( 41,game.getBoard ().getPitsStoneCount ( Player.PLAYER_2 ) + game.getBoard ().getCalaStoneCount ( Player.PLAYER_2 ));
        Assert.assertEquals (Player.PLAYER_2, game.getWinner ());
    }

    @Test
    @DisplayName("play last round of the game without winner")
    void game_lastRoundWithoutWinner(){
        game.getBoard ().getPits ().stream ().filter(pit -> !pit.isCala()).forEach ( pit -> pit.setStoneCount ( 0 ) );

        game.getBoard ().getPit ( Player.PLAYER_1.getCalaIdx () ).setStoneCount ( 35 );
        game.getBoard ().getPit ( Player.PLAYER_2.getCalaIdx () ).setStoneCount ( 34 );
        game.getBoard ().getPit(13).setStoneCount ( 1 );
        game.getBoard ().getPit ( 6 ).setStoneCount ( 2 );

        game.setCurrentPlayer ( Player.PLAYER_2 );

        when(this.gameRepository.findById ( 1002 )).thenReturn ( Optional.of(game));
        game = this.gameService.play ( 1002,13 );
        assertThat ( game.getBoard ().getPitsStoneCount (Player.PLAYER_1 )
                + game.getBoard ().getCalaStoneCount ( Player.PLAYER_1 )).isEqualTo ( 37 );
        assertThat ( game.getBoard ().getPitsStoneCount ( Player.PLAYER_2 )
                + game.getBoard ().getCalaStoneCount ( Player.PLAYER_2 )).isEqualTo ( 35 );
        Assert.assertNull (game.getWinner ());
    }

     @Test
    @DisplayName("if player selected empty pit")
    void play_emptyPit() {
        game.getBoard ().getPit ( 3 ).setStoneCount ( 0 );
        when(gameRepository.findById ( 1000 )).thenReturn (Optional.of(game));

        assertThatThrownBy(() -> this.gameService.play ( 1000,3 ))
                .isInstanceOf( InvalidMoveException.class)
                .hasMessage(String.format("The Pit is empty: %d",3));
    }

    @Test
    @DisplayName("if last pit is cala")
    void play_lastPitIsCala() {
        when(this.board.getPit ( 1 )).thenReturn ( new Pit(1,6,false) );
        when(this.gameRepository.findById ( 1000 )).thenReturn ( Optional.of(game));
        this.gameService.play(1000,1);
        assertThat(this.game.getCurrentPlayer())
                .isEqualTo(Player.PLAYER_1);
    }

    @Test
    @DisplayName("check reset game")
    void resetGame() {
        when(gameRepository.findById ( 1000 )).thenReturn (Optional.of(game));
        gameService.resetGame ( game );
        assertThat (game.getBoard ( ).getPitsStoneCount ( Player.PLAYER_1 )).isEqualTo ( 0 );
        assertThat (game.getBoard ( ).getPitsStoneCount ( Player.PLAYER_2 )).isEqualTo ( 0 );
    }
}