package ir.soodeh.mancala.model;
import static ir.soodeh.mancala.constants.KalahaConstants.*;

public class Pit {

    private int id;
    private int stoneCount;
    private boolean isCala;

    public Pit(int id, int stoneCount, boolean isCala) {
        this.id = id;
        this.stoneCount = stoneCount;
        this.isCala = isCala;
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

    /**
     * this method is for getting the owner of the pit based on pit id
     * @return Player based on pit index
     */
    public Player getOwner(){
        return (this.id <= LAST_IDX/2) ? Player.PLAYER_1 : Player.PLAYER_2;
    }

    public boolean isCala() {
        return (this.id == LAST_IDX/2 || this.id == LAST_IDX);
    }

}
