package ir.soodeh.mancala.services;

import ir.soodeh.mancala.domain.Pit;
import ir.soodeh.mancala.services.exceptions.GameNotFoundException;
import ir.soodeh.mancala.services.exceptions.InvalidMoveException;
import ir.soodeh.mancala.domain.Board;
import ir.soodeh.mancala.domain.Game;
import ir.soodeh.mancala.domain.Player;
import ir.soodeh.mancala.repositories.GameRepository;
import ir.soodeh.mancala.services.exceptions.PitNotFoundException;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

@ExtendWith(MockitoExtension.class)
//@DirtiesContext
class GameServiceTest {

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private GameService gameService;

    private Game game;

    @BeforeEach
    public void setup(){
        game = new Game();
    }

    @Test
    @DisplayName("create a new game")
    void createGame() {
        when(this.gameRepository.create (any(Game.class))).thenReturn (game);
        Game createdGame = this.gameService.createGame ();
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
        game.getBoard ().getPit (13 ).setStoneCount ( 2 );
        game.getBoard ().getPit ( 3 ).setStoneCount ( 5 );
        game.getBoard ().getPit ( 6 ).setStoneCount ( 2 );

        game.setCurrentPlayer ( Player.PLAYER_2 );

        when(this.gameRepository.findById ( 1000 )).thenReturn ( Optional.of(game));
        game = this.gameService.play ( 1000,13 );
        Assert.assertEquals ( 31,game.getBoard ().getStoneCount (Player.PLAYER_1, false ) + game.getBoard ().getStoneCount ( Player.PLAYER_1, true ));
        Assert.assertEquals ( 41,game.getBoard ().getStoneCount ( Player.PLAYER_2, false ) + game.getBoard ().getStoneCount ( Player.PLAYER_2, true ));
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

        when(this.gameRepository.findById ( 1000 )).thenReturn ( Optional.of(game));
        game = this.gameService.play ( 1000,13 );
        assertThat ( game.getBoard ().getStoneCount (Player.PLAYER_1, false )
                + game.getBoard ().getStoneCount ( Player.PLAYER_1, true )).isEqualTo ( 37 );
        assertThat ( game.getBoard ().getStoneCount ( Player.PLAYER_2, false )
                + game.getBoard ().getStoneCount ( Player.PLAYER_2, true )).isEqualTo ( 35 );
        assertNull (game.getWinner ());
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
    @DisplayName("if last pit is kalaha")
    void play_lastPitIsCala() {
        game.getBoard().getPit(1).setStoneCount(6);
        when(this.gameRepository.findById ( 1000 )).thenReturn ( Optional.of(game));
        this.gameService.play(1000,1);
        assertThat(this.game.getCurrentPlayer())
                .isEqualTo(Player.PLAYER_1);
    }

    @Test
    @DisplayName("check reset game")
    void resetGame() {
        gameService.resetGame ( game );
        assertThat (game.getBoard ( ).getStoneCount ( Player.PLAYER_1, false )).isEqualTo ( 0 );
        assertThat (game.getBoard ( ).getStoneCount ( Player.PLAYER_2, false )).isEqualTo ( 0 );
    }

    @Test
    @DisplayName("When the game id is invalid")
    void play_gameNotFound() {
        when(this.gameRepository.findById(1000))
                .thenReturn(Optional.ofNullable(null));
        assertThatThrownBy( () -> this.gameService.play(1000, 2))
                .isInstanceOf(GameNotFoundException.class);
    }

    @Test
    @DisplayName("When the pit id is not in range")
    void play_pitNotFound() {
        when(this.gameRepository.findById ( 1000 )).thenReturn ( Optional.of(game));
        assertThatThrownBy(() -> this.gameService.play(1000,-1))
                .isInstanceOf( PitNotFoundException.class)
                .hasMessage(String.format ( "Selected Pit id Invalid %d",-1));
        assertThatThrownBy(() -> this.gameService.play(1000, 15))
                .isInstanceOf(PitNotFoundException.class)
                .hasMessage(String.format ( "Selected Pit id Invalid %d",15));
    }

    @Test
    @DisplayName("When the pit does not belong to current player")
    void makeMove_invalidPitForCurrentPlayer() {
        when(this.gameRepository.findById ( 1000 )).thenReturn ( Optional.of(game));
        assertThatThrownBy(() -> this.gameService.play(1000,13))
                .isInstanceOf(InvalidMoveException.class)
                .hasMessage(String.format("The Pit does not belong to current player: %d",13));
    }

    @Test
    @DisplayName("When kalaha is selected")
    void makeMove_kalahIsSelected() {
        when(this.gameRepository.findById ( 1000 )).thenReturn ( Optional.of(game));

        assertThatThrownBy(() -> this.gameService.play(1000,7))
                .isInstanceOf(InvalidMoveException.class)
                .hasMessage("The kalaha has been selected");
    }


}