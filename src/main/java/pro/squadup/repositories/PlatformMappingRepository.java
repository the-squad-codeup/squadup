package pro.squadup.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.squadup.models.PlatformMapping;

@Repository
public interface PlatformMappingRepository extends JpaRepository<PlatformMapping, Long> {
    boolean existsByIgdbId(Long igdbId);
}
