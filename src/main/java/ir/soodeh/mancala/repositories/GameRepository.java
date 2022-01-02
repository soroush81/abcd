package ir.soodeh.mancala.repositories;

import ir.soodeh.mancala.domain.Game;

import java.util.Optional;

public interface GameRepository {
    public Optional<Game> findById(Integer id);
    public Game save(Game game);
}
