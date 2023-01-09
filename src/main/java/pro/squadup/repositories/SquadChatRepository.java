package pro.squadup.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.squadup.models.SquadChat;

@Repository
public interface SquadChatRepository extends JpaRepository<SquadChat, Long> {
}
