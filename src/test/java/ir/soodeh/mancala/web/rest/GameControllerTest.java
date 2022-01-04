package ir.soodeh.mancala.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.soodeh.mancala.domain.Board;
import ir.soodeh.mancala.domain.Game;
import ir.soodeh.mancala.domain.Player;
import ir.soodeh.mancala.services.GameService;
import ir.soodeh.mancala.services.dtos.GameDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

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
    private GameService gameService;

    @Test
    void createGame() throws Exception {
        Game createdGame = new Game();
        when(this.gameService.createGame()).thenReturn(createdGame);

        MvcResult mvcResult = mvc.perform(post("/game")
                .accept( MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();


        String json = mvcResult.getResponse().getContentAsString();
        GameDto newGameResponse = objectMapper.readValue(json, GameDto.class);
        assertThat(newGameResponse.getId())
                .isEqualTo(createdGame.getId());

        verify(gameService, times(1)).createGame();

    }

    @Test
    void playGame() throws Exception {

        Game game = new Game();
        when(this.gameService.play("1000", 3))
                .thenReturn(game);

       mvc.perform(put("/game/1000/pit/3")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect( MockMvcResultMatchers.jsonPath("$.status.1").value(6))
                .andExpect( MockMvcResultMatchers.jsonPath("$.currentPlayer").value( Player.PLAYER_1.toString () ))
                .andReturn();

        verify(gameService, times(1)).play("1000", 3);

    }
}