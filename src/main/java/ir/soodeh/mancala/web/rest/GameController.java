package ir.soodeh.mancala.web.rest;

import ir.soodeh.mancala.domain.Game;
import ir.soodeh.mancala.services.GameService;
import ir.soodeh.mancala.services.dtos.GameDto;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/game")
@CrossOrigin(origins = "http://localhost:3000")
public class GameController {

    private GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping()
    public ResponseEntity<Game> createGame(){
        Game game = gameService.createGame (  );
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").
                buildAndExpand(game.getId()).toUri();
        JSONObject gameStatus = game.getBoard ().getGameStatus ();
        GameDto gameDto = new GameDto ( game.getId (), gameStatus , game.getCurrentPlayer(),game.getWinner ());
        return ResponseEntity.created(uri).body(game);
    }

    @PutMapping("{gameId}/pit/{pitId}")
    public ResponseEntity<Game> playGame(@PathVariable Integer gameId, @PathVariable int pitId){
        Game game = gameService.play ( gameId, pitId );

        JSONObject gameStatus = game.getBoard ().getGameStatus ();
        GameDto gameDto = new GameDto ( game.getId (), gameStatus ,game.getCurrentPlayer(), game.getWinner ());
        return ResponseEntity.status ( HttpStatus.OK ).body (game);
    }
}
