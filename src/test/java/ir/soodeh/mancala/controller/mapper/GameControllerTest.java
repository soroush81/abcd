package ir.soodeh.mancala.controller.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.soodeh.mancala.controller.GameController;
import ir.soodeh.mancala.controller.mappers.GameMapper;
import ir.soodeh.mancala.model.Game;
import ir.soodeh.mancala.model.Player;
import ir.soodeh.mancala.services.GameServiceImpl;
import ir.soodeh.mancala.controller.dto.GameDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GameController.class)
class GameControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private GameServiceImpl gameService;

    @MockBean
    private GameMapper gameMapper;

    private Game game;
    private GameDto gameDto;

    @BeforeEach
    public void setup() {
        game = new Game();
        gameDto = new GameDto(game.getId (), game.getBoard ().getStatus (),game.getCurrentPlayer (), game.getWinner ());
        when(this.gameService.createGame()).thenReturn( Optional.of(game));
        when(this.gameMapper.toDto ( game )).thenReturn( gameDto);
    }

    @Test
    void createGame() throws Exception {
        MvcResult mvcResult = mvc.perform(post("/game")
                .accept( MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated ())
                .andReturn();

        String json = mvcResult.getResponse().getContentAsString();
        gameDto = objectMapper.readValue(json, GameDto.class);
        assertThat(gameDto.getId())
                .isEqualTo(game.getId());

        verify(gameService, times(1)).createGame();

    }

    @Test
    void playGame() throws Exception {
        Game game = new Game();
        GameDto gameDto = new GameDto(game.getId (), game.getBoard ().getStatus (),game.getCurrentPlayer (), game.getWinner ());
        when(this.gameService.play("6b09554d-4985-4957-a43f-d9ff327aa930", 3))
                .thenReturn(Optional.of(game));
        when(this.gameMapper.toDto ( game )).thenReturn( gameDto);
       mvc.perform(put("/game/6b09554d-4985-4957-a43f-d9ff327aa930/pit/3")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect( MockMvcResultMatchers.jsonPath("$.status.1").value(6))
                .andExpect( MockMvcResultMatchers.jsonPath("$.currentPlayer").value( Player.PLAYER_1.toString () ))
                .andReturn();

        verify(gameService, times(1)).play("6b09554d-4985-4957-a43f-d9ff327aa930", 3);
    }
}