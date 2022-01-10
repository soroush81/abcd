package ir.soodeh.mancala.services;

import ir.soodeh.mancala.model.Game;
import ir.soodeh.mancala.model.Pit;
import ir.soodeh.mancala.model.Player;
import ir.soodeh.mancala.services.exceptions.GameNotFoundException;
import ir.soodeh.mancala.repositories.GameRepository;
import ir.soodeh.mancala.services.validator.GameValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static ir.soodeh.mancala.constants.KalahaConstants.*;

@Service
public class GameServiceImpl implements GameService {

    private GameRepository gameRepository;
    private GameValidator gameValidator;

    @Autowired
    public GameServiceImpl(GameRepository gameRepository, GameValidator gameValidator) {
        this.gameRepository = gameRepository;
        this.gameValidator = gameValidator;
    }

    public Optional<Game> createGame(){
        return Optional.of(gameRepository.save ( new Game() ));
    }

    public Optional<Game> play(final String gameId, final int pitIdx){
        Optional<Game> game = this.gameRepository.findById ( gameId );
        if (!game.isPresent())
            throw new GameNotFoundException (gameId);
        return game.map ( g -> {
                    this.gameValidator.validateGame ( g, pitIdx );
                    Pit lastPit = shiftStones ( g, pitIdx );
                    getOppositeOnLastEmptyPit(g ,lastPit);
                    decideGameStatus(g,lastPit);
                    return g;
             }
         );

    }

    public void resetGame(final String gameId){
        Game game = this.gameRepository.findById ( gameId ).orElseThrow (()->new GameNotFoundException ( gameId ));
        game.getBoard ( ).getPits ().stream ().filter(pit -> !pit.isCala()).forEach ( pit -> pit.setStoneCount ( 6 ) );
    }

    /**
     * shift the stones of the selected pit in anticlockwise direction one per pit until last one
     * @param game
     * @param pitIdx
     */
    private Pit shiftStones(final Game game,int pitIdx) {
        Pit selectedPit = game.getBoard ().getPit(pitIdx);
        int selectedPitStoneCount = selectedPit.getStoneCount ();
        selectedPit.setStoneCount (0);
        Pit currentPit = null;

        // loop to shift the stone in the pits anticlockwise
        while(selectedPitStoneCount > 0){
            pitIdx++;
            if (pitIdx > LAST_IDX)
                pitIdx = FIRST_IDX;
            currentPit = game.getBoard ().getPit ( pitIdx );
            currentPit.setStoneCount ( currentPit.getStoneCount () + 1 );
            selectedPitStoneCount--;
        }
       return currentPit;
    }


    /**
     *  if game is over decide winner else change current player
     * @param game
     */
    private void decideGameStatus(final Game game,final Pit lastPit) {
        int player1StoneCount = game.getBoard ().getStoneCount (Player.PLAYER_1, false);
        int player2StoneCount = game.getBoard ().getStoneCount (Player.PLAYER_2, false);

        //if game is over decide winner and finish the game
        if (player1StoneCount == 0 || player2StoneCount == 0){
            putRemainderStonesToKalahas ( game, player1StoneCount, player2StoneCount );
            decideWinner(game);
        }
        else{
            changeCurrentPlayer(game, lastPit);
        }
    }

    /**
     * when last stone sit on current player's empty pit, all the opposite side's pit stones will be his/her
     * @param game
     * @param lastPit
     */
    private void getOppositeOnLastEmptyPit(final Game game, final Pit lastPit) {
        final Player currentPlayer = game.getCurrentPlayer ();

        if (lastPit.getStoneCount() == 1 && !lastPit.isCala ()
                && lastPit.getOwner ().equals ( currentPlayer )){

            Pit oppositePit = game.getBoard().getPit ( LAST_IDX-lastPit.getId () );
            Pit currentPlayerKalah = game.getBoard ().getPit(currentPlayer.getCalaIdx ());

            if (oppositePit.getStoneCount () > 0) {
                currentPlayerKalah.setStoneCount ( currentPlayerKalah.getStoneCount ( ) + oppositePit.getStoneCount ( ) + 1 );
                oppositePit.setStoneCount ( 0 );
                lastPit.setStoneCount ( 0 );
            }
        }
    }

    /**
     * put remainder stones of each player to their calas
     * @param game
     * @param player1StoneCount
     * @param player2StoneCount
     */
    private void putRemainderStonesToKalahas(final Game game, final int player1StoneCount, final int player2StoneCount) {
        Pit player1Cala = game.getBoard ().getPit ( Player.PLAYER_1.getCalaIdx () );
        Pit player2Cala = game.getBoard ().getPit ( Player.PLAYER_2.getCalaIdx () );
        player1Cala.setStoneCount (player1Cala.getStoneCount () + player1StoneCount);
        player2Cala.setStoneCount (player2Cala.getStoneCount () + player2StoneCount);
        game.getBoard ( ).getPits ().stream ().filter(pit -> !pit.isCala()).forEach ( pit -> pit.setStoneCount ( 0 ) );
    }

    /**
     * change turn
     * @param game
     */
    private void changeCurrentPlayer(final Game game, final Pit lastPit) {
        if (lastPit.isCala () && lastPit.getId () == game.getCurrentPlayer ().getCalaIdx ()) {
            return;
        }
        if (game.getCurrentPlayer() == Player.PLAYER_1)
            game.setCurrentPlayer( Player.PLAYER_2 );
        else
            game.setCurrentPlayer( Player.PLAYER_1 );
    }

    /**
     * decide winner if the game is over
     * @param game
     */
    private void decideWinner(Game game) {
        int player1CalaStoneCount = game.getBoard ().getStoneCount ( Player.PLAYER_1, true );
        int player2CalaStoneCount = game.getBoard ().getStoneCount ( Player.PLAYER_2, true );

        if (player1CalaStoneCount > player2CalaStoneCount)
            game.setWinner ( Player.PLAYER_1 );
        else if (player1CalaStoneCount == player2CalaStoneCount)
            game.setWinner ( null );
        else
            game.setWinner ( Player.PLAYER_2 );
        game.setCurrentPlayer ( null );
    }
}
