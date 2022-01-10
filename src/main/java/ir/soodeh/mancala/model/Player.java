package ir.soodeh.mancala.model;
import static ir.soodeh.mancala.constants.KalahaConstants.*;

public enum Player {
    PLAYER_1( LAST_IDX/2),
    PLAYER_2(LAST_IDX);

    private final int calaIdx;

    /**
     *
     * @param id of kalaha pit of the player
     */
    Player(int calaIdx) {
        this.calaIdx=calaIdx;
    }

    /**
     * @return id of kalaha pit of the player
     */
    public int getCalaIdx() {
        return calaIdx;
    }

}
