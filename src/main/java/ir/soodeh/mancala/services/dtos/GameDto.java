package ir.soodeh.mancala.services.dtos;

import ir.soodeh.mancala.enums.Player;
import org.json.simple.JSONObject;

public class GameDto {
    private int id;
    private JSONObject status;
    private Player winner;

    public GameDto(int id, JSONObject status, Player winner) {
        this.id = id;
        this.status = status;
        this.winner = winner;
    }

    public int getId() {
        return id;
    }

    public JSONObject getStatus() {
        return status;
    }

    public Player getWinner() {
        return winner;
    }
}