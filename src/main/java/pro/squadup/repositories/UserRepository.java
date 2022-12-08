package pro.squadup.repositories;

import org.springframework.stereotype.Repository;
import pro.squadup.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
