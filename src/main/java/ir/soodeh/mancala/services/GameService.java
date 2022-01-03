package ir.soodeh.mancala.services;

import ir.soodeh.mancala.domain.Game;
import ir.soodeh.mancala.domain.Pit;
import ir.soodeh.mancala.domain.Player;
import ir.soodeh.mancala.services.exceptions.GameNotFoundException;
import ir.soodeh.mancala.repositories.GameRepository;
import ir.soodeh.mancala.services.exceptions.InvalidMoveException;
import ir.soodeh.mancala.services.exceptions.PitNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static ir.soodeh.mancala.constants.KalahaConstants.*;

@Service
public class GameService {

    private GameRepository gameRepository;

    @Autowired
    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    /**
     * this method for creating a new game
     * @return created game object
     */
    public Game createGame(){
        return gameRepository.save ( new Game() );
    }

    /**
     * this method use for playing game
     * @param gameId
     * @param pitIdx
     * @return game object
     */
    public Game play(final Integer gameId, final int pitIdx){
        Game game = this.gameRepository.findById ( gameId ).orElseThrow (()->new GameNotFoundException ( gameId ));
        validateSelectedPit(game, pitIdx);
        Pit lastPit = shiftStones(game, pitIdx);
        getOppositeOnLastEmptyPit(game ,lastPit);
        decideGameStatus(game,lastPit);
        return game;
    }

    /**
     * this method is for resetting game
     * @param game that want to reset
     */
    public void resetGame(final Game game){
        game.getBoard ( ).getPits ().stream ().filter(pit -> !pit.isCala()).forEach ( pit -> pit.setStoneCount ( 0 ) );
    }

    /**
     * shift the stones of the selected pit in anticlockwise direction one per pit until last one
     * @param game
     * @param pitIdx
     */
    private Pit shiftStones(final Game game,int pitIdx) {
        //shift stones in a loop
        Pit selectedPit = game.getBoard ().getPit(pitIdx);
        int selectedPitStoneCount = selectedPit.getStoneCount ();
        selectedPit.setStoneCount (0);
        Pit currentPit = null;
        // loop to place the stone in the pits anticlockwise
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
        if (lastPit.getStoneCount() == 1 && !lastPit.isCala () && lastPit.getOwner ().equals ( currentPlayer )){
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
        resetGame (game );
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

    /**
     * this methods is for validation rules for move
     * @param game
     * @param pitIdx
     */
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
            throw new InvalidMoveException("The Pit is empty",pitIdx);
        }
    }
}
