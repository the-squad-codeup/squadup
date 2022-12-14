package pro.squadup.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.squadup.models.Location;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    Location findByTimezone(String timezone);
}
