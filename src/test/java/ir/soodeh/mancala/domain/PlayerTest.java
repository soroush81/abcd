package ir.soodeh.mancala.domain;

import static ir.soodeh.mancala.constants.KalahaConstants.*;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PlayerTest {

    @Test
    void getCalaIdx() {
        assertThat(Player.PLAYER_1.getCalaIdx ())
                .isEqualTo( LAST_IDX/2 );
        assertThat(Player.PLAYER_2.getCalaIdx ())
                .isEqualTo(LAST_IDX);
    }


}