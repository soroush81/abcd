package ir.soodeh.mancala.model;

import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BoardTest {

    private Board board;

    @BeforeEach
    public void setup(){
        board = new Board();
    }

    @Test
    void getStoneCount() {
        assertThat(board.getStoneCount (  Player.PLAYER_1, false)).isEqualTo ( 36 );
    }

    @Test
    void getStatus() {
        JSONObject status = this.board.getStatus();

        assertThat(status.get(1))
                .isEqualTo(6);

        assertThat(status.get(12))
                .isEqualTo(6);

        assertThat(status.get(7))
                .isEqualTo(0);
    }
}