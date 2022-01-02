package ir.soodeh.mancala.domain;

public enum Player {
    PLAYER_1( Board.LAST_IDX/2),
    PLAYER_2(Board.LAST_IDX);

    private final int calaIdx;

    Player(int calaIdx) {
        this.calaIdx=calaIdx;
    }

    public int getCalaIdx() {
        return calaIdx;
    }

}
