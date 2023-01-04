package pro.squadup.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.squadup.models.Game;
import pro.squadup.models.Preferences;

@Repository
public interface PreferencesRepository extends JpaRepository<Preferences, Long> {
}
