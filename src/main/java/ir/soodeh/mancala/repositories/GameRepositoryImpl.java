package ir.soodeh.mancala.repositories;

import ir.soodeh.mancala.models.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class GameRepositoryImpl implements GameRepository {

    Map<Integer, Game> games =  new HashMap<>();

    @Override
    public Optional<Game> findById(int id) {
        return Optional.of (games.get ( id ));
    }

    @Override
    public Game save(Game game) {
        games.put(game.getId(), game);
        return game;
    }
}
