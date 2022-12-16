package pro.squadup.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.squadup.models.Rating;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    Rating findByRating(String rating);
    Rating findByIgdbId(int igdbId);
}
