package ir.soodeh.mancala.domain;
import static ir.soodeh.mancala.constants.KalahaConstants.*;

public enum Player {
    PLAYER_1( LAST_IDX/2),
    PLAYER_2(LAST_IDX);

    private final int calaIdx;

    Player(int calaIdx) {
        this.calaIdx=calaIdx;
    }

    public int getCalaIdx() {
        return calaIdx;
    }

}
