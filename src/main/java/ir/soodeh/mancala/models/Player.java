package ir.soodeh.mancala.models;

import java.util.List;

public enum Player {
    PLAYER_1( List.of(1,2,3,4,5,6), Board.LAST_IDX/2),
    PLAYER_2(List.of(8,9,10,11,12,13), Board.LAST_IDX);

    private int calaIdx;
    private List<Integer> pitsIndex;

    Player(List<Integer> pitsIndex, int calaIdx) {
        pitsIndex=pitsIndex;
        calaIdx=calaIdx;
    }

    public int getCalaIdx() {
        return calaIdx;
    }

    public List<Integer> getPitsIndex() {
        return pitsIndex;
    }

    public void setPitsIndex(List<Integer> pitsIndex) {
        this.pitsIndex = pitsIndex;
    }
}
