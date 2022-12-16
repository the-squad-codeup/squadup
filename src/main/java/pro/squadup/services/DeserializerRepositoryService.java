package pro.squadup.services;

import org.springframework.stereotype.Service;
import pro.squadup.models.Rating;
import pro.squadup.repositories.GenreRepository;
import pro.squadup.repositories.PlatformRepository;
import pro.squadup.repositories.RatingRepository;

@Service
public class DeserializerRepositoryService {

    private final RatingRepository ratingDao;
    private final GenreRepository genreDao;
    private final PlatformRepository platformDao;

    public DeserializerRepositoryService(RatingRepository ratingDao, GenreRepository genreDao, PlatformRepository platformDao) {
        this.ratingDao = ratingDao;
        this.genreDao = genreDao;
        this.platformDao = platformDao;
    }

    public Rating findRatingByIgdbId(int igdbId) {
        return ratingDao.findByIgdbId(igdbId);
    }
}
