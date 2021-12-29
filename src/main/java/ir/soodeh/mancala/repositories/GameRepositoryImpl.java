package ir.soodeh.mancala.repositories;
import ir.soodeh.mancala.domain.Game;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class GameRepositoryImpl implements GameRepository {

    private Map<Integer, Game> games =  new HashMap<>();

    @Override
    public Game create(Game game) {
        games.put(game.getId(), game);
        return game;
    }

    @Override
    public Optional<Game> findById(Integer id) {
       return Optional.of(games.get ( id ));
    }

}
