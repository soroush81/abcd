package ir.soodeh.mancala.model;

import java.util.UUID;

public class Game {
    private String id;
    private Board board;
    private Player currentPlayer = Player.PLAYER_1;
    private Player winner;

    public Game() {
        this.id = UUID.randomUUID ().toString ();
        this.board = new Board (  );
    }

    public String getId() {
        return id;
    }

    public Board getBoard() {
        return board;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public Player getWinner() {
        return winner;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }
}
