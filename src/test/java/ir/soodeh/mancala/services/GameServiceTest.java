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
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
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
    @DisplayName("check create a new game")
    void createGame() {
        when(this.gameRepository.save (any(Game.class))).thenReturn (game);
        Game createdGame = this.gameService.createGame ();
        Assert.assertNotNull (createdGame);
        Assert.assertEquals (game, createdGame );
    }

    @Test
    @DisplayName("check reset game")
    void resetGame() {
        gameService.resetGame ( game );
        assertThat (game.getBoard ( ).getStoneCount ( Player.PLAYER_1, false )).isEqualTo ( 0 );
        assertThat (game.getBoard ( ).getStoneCount ( Player.PLAYER_2, false )).isEqualTo ( 0 );
    }

    @Nested
    @DisplayName("Play game test")
    class Play {
        @BeforeEach
        void gameSetup() {
            when(gameRepository.findById ( 1000 )).thenReturn (Optional.of(game));
        }

        @Test
        @DisplayName("play one round of game")
        void play_basic() {
            Game playedGame = gameService.play ( 1000,3 );
            assertThat(game).isEqualTo(playedGame);
            verify ( gameRepository, times(1)).findById(1000);
        }

        @Test
        @DisplayName("if player selected empty pit")
        void play_emptyPit() {
            game.getBoard ().getPit ( 3 ).setStoneCount ( 0 );

            assertThatThrownBy(() -> gameService.play ( 1000,3 ))
                    .isInstanceOf( InvalidMoveException.class)
                    .hasMessage(String.format("invalid move: The Pit is empty: %d",3));
        }

        @Test
        @DisplayName ( "if last pit sit on the empty pit of current player")
        void play_lastPitSitOnEmpty(){
            game.getBoard ().getPit ( 4 ).setStoneCount (1);
            game.getBoard ().getPit ( 5 ).setStoneCount (0);
            game.getBoard ().getPit ( 7 ).setStoneCount ( 8 );

            Game playedGame = gameService.play ( 1000,4 );
            assertThat(game.getCurrentPlayer()).isEqualTo(Player.PLAYER_2);
            assertThat ( playedGame.getBoard ().getPit ( 7 ).getStoneCount () ).isEqualTo ( 15 );
            assertThat ( playedGame.getBoard ().getPit ( 5 ).getStoneCount () ).isEqualTo ( 0 );
            assertThat ( playedGame.getBoard ().getPit ( 4 ).getStoneCount () ).isEqualTo ( 0 );
        }

        @Test
        @DisplayName("if last pit is kalaha")
        void play_lastPitIsCala() {
            game.getBoard().getPit(1).setStoneCount(6);
            gameService.play(1000,1);
            assertThat(game.getCurrentPlayer())
                    .isEqualTo(Player.PLAYER_1);
        }

        @Test
        @DisplayName("if the game id is invalid")
        void play_gameNotFound() {
            when(gameRepository.findById(1000))
                    .thenReturn(Optional.ofNullable(null));
            assertThatThrownBy( () -> gameService.play(1000, 2))
                    .isInstanceOf(GameNotFoundException.class);
        }

        @Test
        @DisplayName("When the pit id is not in range")
        void play_pitNotFound() {
            assertThatThrownBy(() -> gameService.play(1000,-1))
                    .isInstanceOf( PitNotFoundException.class)
                    .hasMessage(String.format ( "Not Found: Could not find selected pit %d",-1));
            assertThatThrownBy(() -> gameService.play(1000, 15))
                    .isInstanceOf(PitNotFoundException.class)
                    .hasMessage(String.format ( "Not Found: Could not find selected pit %d",15));
        }

        @Test
        @DisplayName("When the pit does not belong to current player")
        void play_invalidPitForCurrentPlayer() {
            assertThatThrownBy(() -> gameService.play(1000,13))
                    .isInstanceOf(InvalidMoveException.class)
                    .hasMessage(String.format("invalid move: The Pit does not belong to current player: %d",13));
        }

        @Test
        @DisplayName("if kalaha is selected")
        void play_kalahIsSelected() {
            assertThatThrownBy(() -> gameService.play(1000,7))
                    .isInstanceOf(InvalidMoveException.class)
                    .hasMessage("invalid move: The kalaha has been selected");
        }

        @Nested
        @DisplayName("Play Last Round")
        class PlayLastRound {
            @BeforeEach
            void gameSetup() {
                game.getBoard ( ).getPits ( ).stream ( ).filter ( pit -> !pit.isCala ( ) ).forEach ( pit -> pit.setStoneCount ( 0 ) );
                game.getBoard ( ).getPit ( 13 ).setStoneCount ( 2 );
                game.getBoard ( ).getPit ( 3 ).setStoneCount ( 5 );
                game.getBoard ( ).getPit ( 6 ).setStoneCount ( 2 );
            }

            @Test
            @DisplayName("if the game has winner")
            void play_lastRoundWithWinner() {
                game.getBoard ( ).getPit ( Player.PLAYER_1.getCalaIdx ( ) ).setStoneCount ( 23 );
                game.getBoard ( ).getPit ( Player.PLAYER_2.getCalaIdx ( ) ).setStoneCount ( 40 );
                game.setCurrentPlayer ( Player.PLAYER_2 );

                game = gameService.play ( 1000, 13 );
                assertThat ( game.getWinner ( ) ).isEqualTo ( Player.PLAYER_2 );
            }

            @Test
            @DisplayName("if the game is without winner")
            void play_lastRoundWithdraw() {
                game.getBoard ( ).getPit ( Player.PLAYER_1.getCalaIdx ( ) ).setStoneCount ( 28 );
                game.getBoard ( ).getPit ( Player.PLAYER_2.getCalaIdx ( ) ).setStoneCount ( 35 );
                game.setCurrentPlayer ( Player.PLAYER_2 );

                game = gameService.play ( 1000, 13 );
                assertThat(game.getWinner())
                        .isNull();
            }
        }


    }


}