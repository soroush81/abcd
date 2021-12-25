package ir.soodeh.mancala.models;

import java.util.concurrent.atomic.AtomicInteger;

public class Game {
    private static AtomicInteger currentId = new AtomicInteger(0);
    private int id;
    private Board board;
    private Player currentPlayer = Player.PLAYER_1;
    private Player winner;

    public Game() {
        this.id = currentId.getAndIncrement ();
        this.board = new Board (  );
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
