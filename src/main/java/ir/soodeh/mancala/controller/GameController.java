package ir.soodeh.mancala.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ir.soodeh.mancala.controller.dto.GameDto;
import ir.soodeh.mancala.controller.mappers.GameMapper;
import ir.soodeh.mancala.services.GameServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/game")
@Api(value="Kalaha Game")
public class GameController {

    private GameServiceImpl gameService;
    private GameMapper gameMapper;

    @Autowired
    public GameController(GameServiceImpl gameService, GameMapper gameMapper) {
        this.gameService = gameService;
        this.gameMapper = gameMapper;
    }

    @PostMapping()
    @ApiOperation(value = "Create a new game", response = Iterable.class)
    public ResponseEntity<GameDto> createGame(){
        GameDto gameDto = gameService.createGame ().map ( gameMapper::toDto ).get ();
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").
                buildAndExpand(gameDto.getId()).toUri();
        return ResponseEntity.created(uri).body(gameDto);
    }

    @PutMapping("{gameId}/pit/{pitId}")
    @ApiOperation(value = "Play one round of a game by selecting a pit by player", response = Iterable.class)
    public GameDto playGame(@PathVariable String gameId, @PathVariable int pitId){
        return gameService.play ( gameId, pitId ).map ( gameMapper::toDto ).get ();
    }
}
