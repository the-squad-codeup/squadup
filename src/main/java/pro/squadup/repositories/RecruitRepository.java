package pro.squadup.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.squadup.models.Recruit;
import pro.squadup.models.User;

@Repository
public interface RecruitRepository extends JpaRepository<Recruit, Long> {
    boolean existsByUserOneAndUserTwo(User userOne, User userTwo);
}
