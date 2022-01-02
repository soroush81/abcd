package ir.soodeh.mancala.domain;

import java.util.concurrent.atomic.AtomicInteger;

public class Game {
    private static AtomicInteger generatedId = new AtomicInteger(1000);
    private int id;
    private Board board;
    private Player currentPlayer = Player.PLAYER_1;
    private Player winner;

    public Game() {
        this.id = generatedId.getAndIncrement ();
        this.board = new Board (  );
    }

    public int getId() {
        return id;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard() {
        this.board = new Board (  );
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
