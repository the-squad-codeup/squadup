package pro.squadup.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.squadup.models.Squad;
import pro.squadup.models.SquadInvite;
import pro.squadup.models.User;

import java.util.Set;

@Repository
public interface SquadInviteRepository extends JpaRepository<SquadInvite, Long> {
    Set<SquadInvite> findAllBySender(User sender);
    Set<SquadInvite> findAllByRecipient(User recipient);
}
