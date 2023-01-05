package pro.squadup.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.squadup.models.Comrade;
import pro.squadup.models.Recruit;
import pro.squadup.models.User;

@Repository
public interface ComradeRepository extends JpaRepository<Comrade, Long> {
    boolean existsByUserOneAndUserTwo(User userOne, User userTwo);

    Comrade findByUserOneAndUserTwo(User userOne, User userTwo);

}
