package pro.squadup.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.squadup.models.Platform;

@Repository
public interface PlatformRepository extends JpaRepository<Platform, Long> {
    Platform findByType(String type);
    Platform findByIgdbIdsIgdbId(Long igdbId);
    boolean existsByIgdbIdsIgdbId(Long igdbId);
}
