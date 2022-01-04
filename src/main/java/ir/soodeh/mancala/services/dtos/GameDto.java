package ir.soodeh.mancala.services.dtos;

import ir.soodeh.mancala.domain.Player;
import org.json.simple.JSONObject;

public class GameDto {
    private String id;
    private JSONObject status;
    private Player currentPlayer;
    private Player winner;

    public GameDto(String id, JSONObject status, Player currentPlayer, Player winner) {
        this.id = id;
        this.status = status;
        this.winner = winner;
        this.currentPlayer=currentPlayer;
    }

    public String getId() {
        return id;
    }

    public JSONObject getStatus() {
        return status;
    }

    public Player getWinner() {
        return winner;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }
}
