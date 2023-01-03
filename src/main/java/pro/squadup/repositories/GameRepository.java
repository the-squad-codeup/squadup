package pro.squadup.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.squadup.models.Game;
import pro.squadup.models.User;

import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    boolean existsByIgdbId(Long igdbId);
    Game findByIgdbId(Long igdbId);

    List<Game> findAllByUser(User user);
}
