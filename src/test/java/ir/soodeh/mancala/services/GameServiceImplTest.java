package ir.soodeh.mancala.services;

import ir.soodeh.mancala.services.exceptions.GameNotFoundException;
import ir.soodeh.mancala.model.Game;
import ir.soodeh.mancala.model.Player;
import ir.soodeh.mancala.repositories.GameRepository;
import ir.soodeh.mancala.services.validator.GameValidator;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameServiceImplTest {
    private final String GAME_ID = "6b09554d-4985-4957-a43f-d9ff327aa930";

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private GameServiceImpl gameService;

    @Mock
    private GameValidator gameValidator;

    private Game game;

    @BeforeEach
    public void setup(){
        game = new Game();
    }

    @Test
    @DisplayName("check create a new game")
    void createGame() {
        when(this.gameRepository.save (any(Game.class))).thenReturn (game);
        Game createdGame = this.gameService.createGame ().get ();
        assertNotNull (createdGame);
        assertEquals (game, createdGame );
    }

    @Test
    @DisplayName("check reset game")
    void resetGame() {
        when(gameRepository.findById ( GAME_ID )).thenReturn (Optional.of(game));
        gameService.resetGame ( GAME_ID );
        assertThat (game.getBoard ( ).getStoneCount ( Player.PLAYER_1, false )).isEqualTo ( 36 );
        assertThat (game.getBoard ( ).getStoneCount ( Player.PLAYER_2, false )).isEqualTo ( 36 );
    }

    @Nested
    @DisplayName("Play game test")
    class PlayGameTest {
        @Test
        @DisplayName("if the game id is invalid")
        void validateGame_gameNotFound() {
            assertThatThrownBy( () -> gameService.play (GAME_ID, 2))
                    .isInstanceOf( GameNotFoundException.class);
        }

        @Test
        @DisplayName("play one round of game")
        void play_basic() {
            when(gameRepository.findById ( GAME_ID )).thenReturn (Optional.of(game));
            when(gameValidator.validateGame (game, 3)).thenReturn(Optional.of(game));
            Game playedGame = gameService.play ( GAME_ID,3 ).get();
            assertThat(game).isEqualTo(playedGame);
            verify ( gameRepository, times(1)).findById(GAME_ID);
            assertThat(game.getBoard().getPit(3).getStoneCount()).isEqualTo(0);
        }

        @Test
        @DisplayName ( "if last pit sit on the empty pit of current player")
        void play_lastPitSitOnEmpty(){
            game.getBoard ().getPit ( 4 ).setStoneCount (1);
            game.getBoard ().getPit ( 5 ).setStoneCount (0);
            game.getBoard ().getPit ( 7 ).setStoneCount ( 8 );
            game.getBoard ().getPit ( 9 ).setStoneCount ( 1 );
            when(gameRepository.findById ( GAME_ID )).thenReturn (Optional.of(game));
            Game playedGame = gameService.play ( GAME_ID,4 ).get();
            assertThat(game.getCurrentPlayer()).isEqualTo( Player.PLAYER_2);
            assertThat ( playedGame.getBoard ().getPit ( 7 ).getStoneCount () ).isEqualTo ( 10 );
            assertThat ( playedGame.getBoard ().getPit ( 5 ).getStoneCount () ).isEqualTo ( 0 );
            assertThat ( playedGame.getBoard ().getPit ( 4 ).getStoneCount () ).isEqualTo ( 0 );
            assertThat ( playedGame.getBoard ().getPit ( 9 ).getStoneCount () ).isEqualTo ( 0 );
        }

        @Test
        @DisplayName("if last pit is kalaha")
        void play_lastPitIsCala() {
            game.getBoard().getPit(1).setStoneCount(6);
            when(gameRepository.findById ( GAME_ID )).thenReturn (Optional.of(game));
            gameService.play (GAME_ID,1);
            assertThat(game.getCurrentPlayer())
                    .isEqualTo(Player.PLAYER_1);
        }

        @Nested
        @DisplayName("Play Last Round")
        class PlayLastRoundTest {
            @BeforeEach
            void gameSetup() {
                game.getBoard ( ).getPits ( ).stream ( ).filter ( pit -> !pit.isCala ( ) ).forEach ( pit -> pit.setStoneCount ( 0 ) );
                game.getBoard ( ).getPit ( 13 ).setStoneCount ( 2 );
                game.getBoard ( ).getPit ( 3 ).setStoneCount ( 5 );
                game.getBoard ( ).getPit ( 6 ).setStoneCount ( 2 );
                when(gameRepository.findById ( GAME_ID )).thenReturn (Optional.of(game));
            }

            @Test
            @DisplayName("if the game has winner")
            void play_lastRoundWithWinner() {
                game.getBoard ( ).getPit ( Player.PLAYER_1.getCalaIdx ( ) ).setStoneCount ( 23 );
                game.getBoard ( ).getPit ( Player.PLAYER_2.getCalaIdx ( ) ).setStoneCount ( 40 );
                game.setCurrentPlayer ( Player.PLAYER_2 );

                game = gameService.play ( GAME_ID, 13 ).get();
                assertThat ( game.getWinner ( ) ).isEqualTo ( Player.PLAYER_2 );
            }

            @Test
            @DisplayName("if the game is without winner")
            void play_lastRoundWithdraw() {
                game.getBoard ( ).getPit ( Player.PLAYER_1.getCalaIdx ( ) ).setStoneCount ( 28 );
                game.getBoard ( ).getPit ( Player.PLAYER_2.getCalaIdx ( ) ).setStoneCount ( 35 );
                game.setCurrentPlayer ( Player.PLAYER_2 );

                game = gameService.play ( GAME_ID, 13 ).get ();
                assertThat(game.getWinner())
                        .isNull();
            }
        }
    }
}