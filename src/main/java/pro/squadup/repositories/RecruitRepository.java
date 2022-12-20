package pro.squadup.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.squadup.models.Recruit;
import pro.squadup.models.User;

import java.util.Set;

@Repository
public interface RecruitRepository extends JpaRepository<Recruit, Long> {
    boolean existsByUserOneAndUserTwo(User userOne, User userTwo);

    Set<Recruit> findAllByUserOne(User userOne);

    Recruit findByUserOneAndUserTwo(User userOne, User userTwo);

}
