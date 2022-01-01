package ir.soodeh.mancala.web.rest;

import ir.soodeh.mancala.domain.Game;
import ir.soodeh.mancala.services.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode= DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class GameControllerTest {

    @InjectMocks
    GameController gameController;

    @Mock
    GameService gameService;

    Game game;

    @BeforeEach
    void setUp() {
        this.game = new Game();
//        this.game = Mockito.spy(game);
    }

    @Test
    @DirtiesContext(methodMode= DirtiesContext.MethodMode.AFTER_METHOD)
    void createGame() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        when(this.gameService.createGame()).thenReturn(game);
        ResponseEntity<Game> responseEntity = gameController.createGame();

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(201);
        assertThat(responseEntity.getHeaders().getLocation().getPath()).isEqualTo("/1000");
    }

    @Test
    @DirtiesContext(methodMode= DirtiesContext.MethodMode.AFTER_METHOD)
    void playGame() {

        when(this.gameService.play(1000,3)).thenReturn(game);
        ResponseEntity<Game> responseEntity = gameController.playGame(1000,3);
    }
}