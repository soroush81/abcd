package ir.soodeh.mancala.models;

public class Pit {

    private int id;
    private int stoneCount;
    private boolean isCala;

    public Pit(int id, int stoneCount) {
        this.id = id;
        this.stoneCount = stoneCount;
    }

    public int getId() {
        return id;
    }

    public int getStoneCount() {
        return stoneCount;
    }

    public void setStoneCount(int stoneCount) {
        this.stoneCount = stoneCount;
    }

    public Player getOwner(){
        return (this.id <= Board.LAST_IDX/2) ? Player.PLAYER_1 : Player.PLAYER_2;
    }

    public boolean isCala() {
        return (this.id == Board.LAST_IDX/2 || this.id == Board.LAST_IDX);
    }

}
