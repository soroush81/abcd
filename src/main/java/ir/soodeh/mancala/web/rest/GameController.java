package ir.soodeh.mancala.web.rest;

import ir.soodeh.mancala.domain.Game;
import ir.soodeh.mancala.services.GameServiceImpl;
import ir.soodeh.mancala.services.dtos.GameDto;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/game")
//@CrossOrigin(origins = "*")
public class GameController {

    private GameServiceImpl gameService;

    @Autowired
    public GameController(GameServiceImpl gameService) {
        this.gameService = gameService;
    }

    @PostMapping()
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
    public GameDto playGame(@PathVariable String gameId, @PathVariable int pitId){
        Game game = gameService.play ( gameId, pitId );
        JSONObject gameStatus = game.getBoard ().getStatus ();
        return new GameDto ( game.getId (), gameStatus ,game.getCurrentPlayer(), game.getWinner ());
    }
}
