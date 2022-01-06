package ir.soodeh.mancala.web.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ir.soodeh.mancala.domain.Game;
import ir.soodeh.mancala.services.GameServiceImpl;
import ir.soodeh.mancala.services.dtos.GameDto;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/game")
@Api(value="Mancala Game")
public class GameController {

    private GameServiceImpl gameService;

    @Autowired
    public GameController(GameServiceImpl gameService) {
        this.gameService = gameService;
    }

    @PostMapping()
    @ApiOperation(value = "Create a new game", response = Iterable.class)
    public GameDto createGame(){
        Game game = gameService.createGame (  );
//        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").
//                buildAndExpand(game.getId()).toUri();
        JSONObject gameStatus = game.getBoard ().getStatus ();
        GameDto gameDto = new GameDto ( game.getId (), gameStatus , game.getCurrentPlayer(),game.getWinner ());
        return gameDto;
                //ResponseEntity.created(uri).body(gameDto);
    }

    @PutMapping("{gameId}/pit/{pitId}")
    @ApiOperation(value = "Play one round of a game by selecting a pit by player", response = Iterable.class)
    public GameDto playGame(@PathVariable String gameId, @PathVariable int pitId){
        Game game = gameService.play ( gameId, pitId );
        JSONObject gameStatus = game.getBoard ().getStatus ();
        return new GameDto ( game.getId (), gameStatus ,game.getCurrentPlayer(), game.getWinner ());
    }
}
