package pro.squadup.services;

import org.springframework.stereotype.Service;
import pro.squadup.models.*;
import pro.squadup.repositories.ComradeRepository;
import pro.squadup.repositories.GenreRepository;
import pro.squadup.repositories.RecruitRepository;
import pro.squadup.repositories.UserRepository;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
public class RecruitMatchingService {

    private UserRepository userDao;
    private ComradeRepository comradeDao;
    private RecruitRepository recruitDao;
    private GenreRepository genreDao;

    public RecruitMatchingService(
            UserRepository userDao,
            ComradeRepository comradeDao,
            RecruitRepository recruitDao,
            GenreRepository genreDao
    ) {
        this.userDao = userDao;
        this.comradeDao = comradeDao;
        this.recruitDao = recruitDao;
        this.genreDao = genreDao;
    }

    public void matchAllRecruits() {
        Set<User> allUsers = new HashSet<>(userDao.findAll());
        for(User user : allUsers) {
            matchRecruits(user, allUsers);
        }
    }

    public void matchRecruits(User user, Set<User> allUsers) {
        for(User otherUser : allUsers) {
            if(
                    user.getId() != otherUser.getId() &&
                    !areComrades(user, otherUser) &&
                    !alreadyRecruited(user, otherUser) &&
                    containsMatchingPlatform(user, otherUser) &&
                    (
                            containsMatchingGames(user, otherUser) ||
                            containsMatchingGenres(user, otherUser)
                    )
            ) {
                addRecruits(user, otherUser);
            }
        }
    }

    private boolean areComrades(User user1, User user2) {
        return comradeDao.existsByUserOneAndUserTwo(user1, user2);
    }

    private boolean alreadyRecruited(User user1, User user2) {
        return comradeDao.existsByUserOneAndUserTwo(user1, user2);
    }

    private boolean containsMatchingPlatform(User user1, User user2) {
        Set<Platform> user1Platforms = user1.getPreferences().getPlatforms();
        Set<Platform> user2Platforms = user2.getPreferences().getPlatforms();
        for(Platform platform1 : user1Platforms) {
            if(user2Platforms.contains(platform1)) {
                return true;
            }
        }
        return false;
    }

    private boolean containsMatchingGames(User user1, User user2) {
        Set<Game> user1Games = user1.getPreferences().getGames();
        Set<Game> user2Games = user2.getPreferences().getGames();
        for(Game game1 : user1Games) {
            if(user2Games.contains(game1)) {
                return true;
            }
        }
        return false;
    }

    private boolean containsMatchingGenres(User user1, User user2) {
        Set<Genre> user1Genres = extractUserGenres(user1);
        Set<Genre> user2Genres = extractUserGenres(user2);
        for(Genre genre1 : user1Genres) {
            for(Genre genre2 : user2Genres) {
                if(genre1.getId() == genre2.getId()) {
                    return true;
                }
            }
        }
        return false;
    }

    private Set<Genre> extractUserGenres(User user) {
        Set<Genre> allGenres = new HashSet<>();
        allGenres.addAll(user.getPreferences().getGenres());
        for(Game game : user.getPreferences().getGames()) {
            for(Genre genre : game.getGenres()) {
                if(!allGenres.contains(genre)) {
                    allGenres.add(genreDao.findById(genre.getId()).get());
                }
            }
        }
        return allGenres;
    }

    public void addRecruits(User user1, User user2) {
        Timestamp currentTimeAndDate = new Timestamp(new Date().getTime());
        Recruit newRecruit = new Recruit(currentTimeAndDate, false);
        newRecruit.setUserOne(user1);
        newRecruit.setUserTwo(user2);
        recruitDao.save(newRecruit);
    }
}
