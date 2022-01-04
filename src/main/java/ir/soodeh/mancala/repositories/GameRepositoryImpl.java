package ir.soodeh.mancala.repositories;
import ir.soodeh.mancala.domain.Game;
import org.springframework.stereotype.Repository;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class GameRepositoryImpl implements GameRepository {

    private Map<String, Game> games =  new ConcurrentHashMap<> ();

    @Override
    public Game save(Game game) {
        games.put(game.getId(), game);
        return game;
    }

    @Override
    public Optional<Game> findById(String id) {
       return Optional.ofNullable(games.get ( id ));
    }

}
