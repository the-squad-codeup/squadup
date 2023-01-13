package pro.squadup.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.squadup.models.Squad;
import pro.squadup.models.User;

import java.util.Set;

@Repository
public interface SquadRepository extends JpaRepository<Squad, Long> {
    Set<Squad> findAllByMembers(User members);
    Set<Squad> findAllByOwner(User owner);
}
