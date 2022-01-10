package ir.soodeh.mancala.services.validator;

import ir.soodeh.mancala.model.Game;
import ir.soodeh.mancala.services.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class GameValidatorTest {
    private Game game;

    @InjectMocks
    private GameValidator gameValidator;

    @BeforeEach
    public void setup(){
        game = new Game();
    }

    @Test
    @DisplayName("if player selected empty pit")
    void validateGame_emptyPit() {
        game.getBoard ().getPit ( 3 ).setStoneCount ( 0 );
        assertThatThrownBy(() -> gameValidator.validateGame ( game,3 ))
                .isInstanceOf( InvalidMoveException.class)
                .hasMessage(String.format("The Pit is empty: %d",3));
    }

    @Test
    @DisplayName("if the pit id is not in range")
    void validateGame_pitNotFound() {
        assertThatThrownBy(() -> gameValidator.validateGame (game,-1))
                .isInstanceOf( PitNotFoundException.class)
                .hasMessage(String.format ( "Could not find selected pit %d",-1));

        assertThatThrownBy(() -> gameValidator.validateGame (game, 15))
                .isInstanceOf( PitNotFoundException.class)
                .hasMessage(String.format ( "Could not find selected pit %d",15));
    }

    @Test
    @DisplayName("if the pit does not belong to current player")
    void validateGame_invalidPitForCurrentPlayer() {
        assertThatThrownBy(() -> gameValidator.validateGame (game,13))
                .isInstanceOf( InvalidMoveException.class)
                .hasMessage(String.format("The Pit does not belong to current player: %d",13));
    }

    @Test
    @DisplayName("if kalaha is selected")
    void validateGame_kalahIsSelected() {
        assertThatThrownBy(() -> gameValidator.validateGame (game,7))
                .isInstanceOf( InvalidMoveException.class)
                .hasMessage("The kalaha has been selected");
    }
}