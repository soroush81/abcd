package ir.soodeh.mancala.services;

import ir.soodeh.mancala.domain.Board;
import ir.soodeh.mancala.domain.Game;
import ir.soodeh.mancala.domain.Pit;
import ir.soodeh.mancala.domain.Player;
import ir.soodeh.mancala.services.exceptions.GameNotFoundException;
import ir.soodeh.mancala.repositories.GameRepository;
import ir.soodeh.mancala.services.exceptions.InvalidMoveException;
import ir.soodeh.mancala.services.exceptions.PitNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return gameRepository.create ( new Game() );
    }

    /**
     * this method use for playing game
     * @param gameId
     * @param pitIdx
     * @return game object
     */
    public Game play(Integer gameId, int pitIdx){
        Game game = this.gameRepository.findById ( gameId ).orElseThrow (()->new GameNotFoundException ( gameId ));
        validateSelectedMove(game, pitIdx);
        Pit lastPit = shiftStones(game, pitIdx);
        decideGameStatus(game,lastPit);
        return game;
    }

    /**
     * this method is for resetting game
     * @param game that want to reset
     */
    public void resetGame(Game game){
        game.getBoard ( ).getPits ().stream ().filter(pit -> !pit.isCala()).forEach ( pit -> pit.setStoneCount ( 0 ) );
    }

    /**
     * shift the stones of the selected pit in anticlockwise direction one per pit until last one
     * @param game
     * @param pitIdx
     */
    private Pit shiftStones(Game game,int pitIdx) {
        //shift stones in a loop
        Pit selectedPit = game.getBoard ().getPit(pitIdx);
        int selectedPitStoneCount = selectedPit.getStoneCount ();
        selectedPit.setStoneCount (0);
        Pit currentPit = null;
        // loop to place the stone in the pits anticlockwise
        while(selectedPitStoneCount > 0){
            pitIdx++;
            if (pitIdx > Board.LAST_IDX)
                pitIdx = Board.FIRST_IDX;
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
    private void decideGameStatus(Game game,Pit lastPit) {
        int player1StoneCount = game.getBoard ().getPitsStoneCount (Player.PLAYER_1);
        int player2StoneCount = game.getBoard ().getPitsStoneCount (Player.PLAYER_2);

        //if game is over decide winner and finish the game
        if (player1StoneCount == 0 || player2StoneCount == 0){
            decideWinner(game);
            putRemainderStonesToCalas ( game, player1StoneCount, player2StoneCount );
            resetGame(game);
        }
        //else change turn
        else{
            changeCurrentPlayer(game, lastPit);
        }
    }

    /**
     * put remainder stones of each player to their calas
     * @param game
     * @param player1StoneCount
     * @param player2StoneCount
     */
    private void putRemainderStonesToCalas(Game game, int player1StoneCount, int player2StoneCount) {
        Pit player1Cala = game.getBoard ().getPit ( Player.PLAYER_1.getCalaIdx () );
        Pit player2Cala = game.getBoard ().getPit ( Player.PLAYER_2.getCalaIdx () );
        player1Cala.setStoneCount (player1Cala.getStoneCount () + player1StoneCount);
        player2Cala.setStoneCount (player2Cala.getStoneCount () + player2StoneCount);

    }

    /**
     * change turn
     * @param game
     */
    private void changeCurrentPlayer(Game game, Pit lastPit) {
        if (lastPit.isCala ()) {
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
        int player1CalaStoneCount = game.getBoard ().getCalaStoneCount ( Player.PLAYER_1 );
        int player2CalaStoneCount = game.getBoard ().getCalaStoneCount ( Player.PLAYER_2 );
        if (player1CalaStoneCount > player2CalaStoneCount)
            game.setWinner ( Player.PLAYER_1 );
        else if (player1CalaStoneCount == player2CalaStoneCount)
            game.setWinner ( null );
        else
            game.setWinner ( Player.PLAYER_2 );
    }

    /**
     * this methods is for validation rules for move
     * @param game
     * @param pitIdx
     */
    private void validateSelectedMove(Game game, int pitIdx) {
        if (pitIdx < 1 || pitIdx > Board.LAST_IDX)
            throw new PitNotFoundException ( pitIdx );
        if (pitIdx == Board.LAST_IDX/2 || pitIdx == Board.LAST_IDX)
            throw new InvalidMoveException ( "The kalaha has been selected" );
        if ((game.getCurrentPlayer () == Player.PLAYER_1 && pitIdx > Board.LAST_IDX/2) ||
                (game.getCurrentPlayer () == Player.PLAYER_2 && pitIdx <= Board.LAST_IDX/2))
            throw new InvalidMoveException ("The Pit does not belong to current player", pitIdx);

        Pit selectedPit = game.getBoard ().getPit ( pitIdx );
        if (selectedPit.getStoneCount() == 0) {
            throw new InvalidMoveException("The Pit is empty",pitIdx);
        }
    }
}
