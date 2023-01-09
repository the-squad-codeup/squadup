package pro.squadup.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.squadup.models.SquadPicture;

@Repository
public interface SquadPictureRepository extends JpaRepository<SquadPicture, Long> {
}
