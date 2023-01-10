package pro.squadup.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.squadup.models.Squad;
import pro.squadup.models.SquadChatMessage;

import java.util.Set;

@Repository
public interface SquadChatMessageRepository extends JpaRepository<SquadChatMessage, Long> {
    Set<SquadChatMessage> findAllBySquad(Squad squad);
}
