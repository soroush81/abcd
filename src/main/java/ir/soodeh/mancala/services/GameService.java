package ir.soodeh.mancala.services;

import ir.soodeh.mancala.models.Board;
import ir.soodeh.mancala.models.Game;
import ir.soodeh.mancala.models.Pit;
import ir.soodeh.mancala.models.Player;
import ir.soodeh.mancala.repositories.GameRepository;
import ir.soodeh.mancala.services.exceptions.GameNotFoundException;
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

    public Game createGame(){
        return gameRepository.save ( new Game() );
    }

    public Game play(int gameId, int pitIdx){
        Game game = gameRepository.findById ( gameId ).orElseThrow (() -> new GameNotFoundException(gameId));
        Pit selectedPit = game.getBoard ().getPit(pitIdx);

        validateSelectedMove(game, selectedPit);
        shiftStones(game, selectedPit);
        return game;
    }

    public void reset(Game game){
        game.getBoard ( ).getPits ().stream ().filter(pit -> !pit.isCala()).forEach ( pit -> pit.setStoneCount ( 0 ) );
    }

    private void shiftStones(Game game,Pit selectedPit) {
        //move stones in a loop
        int selectedPitStoneCount = selectedPit.getStoneCount ();
        int pitIdx = selectedPit.getId ();
        selectedPit.setStoneCount ( 0 );
        Pit currentPit;
        while(selectedPitStoneCount > 0){
            pitIdx++;
            if (pitIdx > Board.LAST_IDX)
                pitIdx = Board.FIRST_IDX;
            currentPit = game.getBoard ().getPit ( pitIdx );
            currentPit.setStoneCount ( currentPit.getStoneCount () + 1 );
            selectedPitStoneCount--;
        }

        //check if game is over decide winner else change current player
        decideGameStatus(game);
    }



    private void decideGameStatus(Game game) {
        checkGameIsOver(game);
    }

    private boolean checkGameIsOver(Game game) {
        int player1StoneCount = game.getBoard ().getPits ().stream ().filter(pit -> pit.getOwner () == Player.PLAYER_1 && pit.getStoneCount () > 0).mapToInt(i -> i.getStoneCount ()).sum();
        int player2StoneCount = game.getBoard ().getPits ().stream ().filter(pit -> pit.getOwner () == Player.PLAYER_1 && pit.getStoneCount () > 0).mapToInt(i -> i.getStoneCount ()).sum();


        if (player1StoneCount == 0 || player2StoneCount == 0){
            Pit player1Cala = game.getBoard ().getPit ( Player.PLAYER_1.getCalaIdx () );
            Pit player2Cala = game.getBoard ().getPit ( Player.PLAYER_2.getCalaIdx () );
            player1Cala.setStoneCount (player1Cala.getStoneCount () + player1StoneCount);
            player2Cala.setStoneCount (player2Cala.getStoneCount () + player2StoneCount);
            decideWinner(game);

            return true;
        }
        else{
            changeCurrentPlayer(game);
            return false;
        }
    }

    private void changeCurrentPlayer(Game game) {
        if (game.getCurrentPlayer() == Player.PLAYER_1)
            game.setCurrentPlayer( Player.PLAYER_2 );
        else
            game.setCurrentPlayer( Player.PLAYER_1 );
    }

    private void decideWinner(Game game) {
        int player1CalaStoneCount = game.getBoard ().getCalaStoneCount ( Player.PLAYER_1 );
        int player2CalaStoneCount = game.getBoard ().getCalaStoneCount ( Player.PLAYER_2 );
        if (player1CalaStoneCount > player2CalaStoneCount)
            game.setWinner ( Player.PLAYER_1 );
        else
            game.setWinner ( Player.PLAYER_2 );
    }

    private void validateSelectedMove(Game game, Pit selectedPit) {
        int pitIdx = selectedPit.getId ();
        if (pitIdx == 0 || pitIdx>Board.LAST_IDX)
            throw new PitNotFoundException ( pitIdx );
        if (pitIdx == Board.LAST_IDX/2 || pitIdx == Board.LAST_IDX)
            throw new InvalidMoveException ( "Cala has been selected" );
        if ((game.getCurrentPlayer () == Player.PLAYER_1 && pitIdx > Board.LAST_IDX/2) ||
                (game.getCurrentPlayer () == Player.PLAYER_2 && pitIdx <= Board.LAST_IDX/2))
            throw new InvalidMoveException ("The Pit does not belong to current player", pitIdx);
        if (selectedPit.getStoneCount() == 0) {
            throw new InvalidMoveException("The Pit is empty,",pitIdx);
        }

    }
}
