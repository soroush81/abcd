package ir.soodeh.mancala.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PitTest {

    private Pit pit;

    @BeforeEach
    public void setup(){
         pit = new Pit(2, 1, false);
    }

    @Test
    void getOwner() {
        assertThat(pit.getOwner ()).isEqualTo ( Player.PLAYER_1 );
    }

    @Test
    void isCala() {
        assertThat ( pit.isCala () ).isEqualTo ( false );
    }
}