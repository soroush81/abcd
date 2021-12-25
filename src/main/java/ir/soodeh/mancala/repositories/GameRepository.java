package ir.soodeh.mancala.repositories;

import ir.soodeh.mancala.models.Game;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface GameRepository {
    public Optional<Game> findById(int id);
    public Game save(Game game);
}
