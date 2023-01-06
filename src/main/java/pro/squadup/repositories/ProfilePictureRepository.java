package pro.squadup.repositories;

import pro.squadup.models.ProfilePicture;
import pro.squadup.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfilePictureRepository extends JpaRepository<ProfilePicture, Long> {
    ProfilePicture findByUser(User user);
    boolean existsByUser(User user);
}
