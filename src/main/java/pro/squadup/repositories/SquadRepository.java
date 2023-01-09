package pro.squadup.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.squadup.models.Squad;

@Repository
public interface SquadRepository extends JpaRepository<Squad, Long> {
}
