package pro.squadup.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.squadup.models.Profile;

@Repository
public interface ProfilesRepository extends JpaRepository<Profile, Long> {
}
