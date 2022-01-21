package ir.soodeh.mancala.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import ir.soodeh.mancala.model.Player;
import org.json.simple.JSONObject;

public class GameDto {
    private String id;
    private JSONObject status;
    private Player currentPlayer;
    private Player winner;

    public GameDto(@JsonProperty("id") String id, @JsonProperty("status") JSONObject status, @JsonProperty("currentPlayer")Player currentPlayer, @JsonProperty("winner")Player winner) {
        this.id = id;
        this.status = status;
        this.winner = winner;
        this.currentPlayer=currentPlayer;
    }

    public String getId() {
        return id;
    }

    public JSONObject getStatus() {
        return this.status;
    }

    public void setStatus(JSONObject status) {
        this.status = status;
    }

    public Player getWinner() {
        return winner;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }
}
