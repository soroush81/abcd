package ir.soodeh.mancala.repositories;

import ir.soodeh.mancala.models.Game;

public interface GameRepository {
    public Game findById(Integer id);
    public Game create(Game game);
}
