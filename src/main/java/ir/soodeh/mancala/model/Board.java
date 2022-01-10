package ir.soodeh.mancala.model;

import org.json.simple.JSONObject;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import static ir.soodeh.mancala.constants.KalahaConstants.*;
import static ir.soodeh.mancala.utils.ManCalaUtils.isCala;

public class Board {

    private List<Pit> pits;

    public Board() {
        //initialize the board
        pits = IntStream.range(FIRST_IDX, LAST_IDX + 1).boxed().map(i-> new Pit(i, isCala ( i ) ? 0 : DEFAULT_PIT_STONE, isCala ( i ) )).collect( Collectors.toList());
    }

    public Pit getPit(int idx){
        return pits.get ( idx - 1);
    }

    public List<Pit> getPits(){
        return pits;
    }

    public int getStoneCount(Player player, boolean isCala){
        return pits.stream().filter ( pit -> pit.getOwner () == player && pit.isCala()==isCala).mapToInt ( Pit::getStoneCount ).sum ();
    }

    /**
     * create a json object that return board status that it key is pitId and its value is pit stone count
     * @return jsonObject of status of the board
     */
    public JSONObject getStatus(){
        JSONObject status = new JSONObject (  );
        pits.stream ().forEach ( pit -> status.put(pit.getId (), pit.getStoneCount ()) );
        return status;
    }

}
