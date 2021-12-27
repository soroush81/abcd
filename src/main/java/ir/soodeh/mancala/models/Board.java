package ir.soodeh.mancala.models;

import ir.soodeh.mancala.enums.Player;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static ir.soodeh.mancala.utils.manCalaUtils.isCala;

public class Board {
    public static final int FIRST_IDX = 1;
    public static final int LAST_IDX = 14;
    private List<Pit> pits = new ArrayList<Pit> (  );

    public Board() {
        //initialize the board
        pits = IntStream.range(FIRST_IDX, LAST_IDX + 1).boxed().map(i-> new Pit(i, isCala ( i ) ? 0 : 6, isCala ( i ) )).collect( Collectors.toList());
    }

    public Pit getPit(int idx){
        return pits.get ( idx - 1);
    }

    public List<Pit> getPits(){
        return pits;
    }

    public int getPitsStoneCount(Player player){
        return pits.stream().filter ( pit -> pit.getOwner () == player && !pit.isCala()).mapToInt ( i->i.getStoneCount () ).sum ();
    }

    public int getCalaStoneCount(Player player){
        return pits.stream().filter ( pit -> pit.getOwner () == player && pit.isCala()).mapToInt ( i->i.getStoneCount () ).sum ();
    }

    public JSONObject getGameStatus(){
        JSONObject status = new JSONObject (  );
        pits.stream ().forEach ( pit -> status.put(pit.getId (), pit.getStoneCount ()) );
        return status;
    }

}
