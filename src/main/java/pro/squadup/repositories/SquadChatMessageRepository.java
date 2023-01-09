package pro.squadup.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.squadup.models.SquadChatMessage;

@Repository
public interface SquadChatMessageRepository extends JpaRepository<SquadChatMessage, Long> {
}
