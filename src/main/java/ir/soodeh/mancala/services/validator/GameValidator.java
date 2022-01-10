package ir.soodeh.mancala.services.validator;

import ir.soodeh.mancala.model.Game;
import ir.soodeh.mancala.model.Pit;
import ir.soodeh.mancala.model.Player;
import ir.soodeh.mancala.services.exceptions.InvalidMoveException;
import ir.soodeh.mancala.services.exceptions.InvalidMoveProblem;
import ir.soodeh.mancala.services.exceptions.PitNotFoundException;
import ir.soodeh.mancala.services.exceptions.PitNotFoundProblem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Optional;

import static ir.soodeh.mancala.constants.KalahaConstants.FIRST_IDX;
import static ir.soodeh.mancala.constants.KalahaConstants.LAST_IDX;

@Component
public class GameValidator {

    public Optional<Game> validateGame(final Game game, final int pitIdx) {
        return Optional.of ( game )
                .map ( g -> {
                    validateSelectedPit ( g, pitIdx );
                    return g;
                } );
    }

    private void validateSelectedPit(final Game game, final int pitIdx) {
        if (pitIdx <  FIRST_IDX || pitIdx > LAST_IDX)
            throw new PitNotFoundException ( pitIdx );

        if (pitIdx == LAST_IDX/2 || pitIdx == LAST_IDX)
            throw new InvalidMoveException ( "The kalaha has been selected" );

        if ((game.getCurrentPlayer () == Player.PLAYER_1 && pitIdx > LAST_IDX/2) ||
                (game.getCurrentPlayer () == Player.PLAYER_2 && pitIdx <= LAST_IDX/2))
            throw new InvalidMoveException ("The Pit does not belong to current player", pitIdx);

        Pit selectedPit = game.getBoard ().getPit ( pitIdx );
        if (selectedPit.getStoneCount() == 0) {
            throw new InvalidMoveException ("The Pit is empty",pitIdx);
        }
    }
}
