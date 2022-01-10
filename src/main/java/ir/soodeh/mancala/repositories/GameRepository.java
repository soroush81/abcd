package ir.soodeh.mancala.repositories;

import ir.soodeh.mancala.model.Game;

import java.util.Optional;

public interface GameRepository {
    Optional<Game> findById(String id);
    Game save(Game game);
}
