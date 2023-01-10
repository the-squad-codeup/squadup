package pro.squadup.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.squadup.models.LastSeenMessage;
import pro.squadup.models.Squad;
import pro.squadup.models.User;

@Repository
public interface LastSeenMessageInterface extends JpaRepository<LastSeenMessage, Long> {
    LastSeenMessage findByUserAndSquad(User user, Squad squad);
}
