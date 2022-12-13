package pro.squadup.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import pro.squadup.models.Genre;
import pro.squadup.models.User;
import pro.squadup.models.Game;
import pro.squadup.repositories.GenreRepository;
import pro.squadup.repositories.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
@EnableScheduling
public class RecruitScheduleConfiguration {

    private UserRepository userDao;
    private GenreRepository genreDao;

    public RecruitScheduleConfiguration(UserRepository userDao, GenreRepository genreDao) {
        this.userDao = userDao;
        this.genreDao = genreDao;
    }

//    @Scheduled(fixedRateString = "${recruit.matcher.delay.in.milliseconds}")
//    public void matchRecruits() {
//        Set<User> allUsers = new HashSet<>(userDao.findAll());
//        for(User user : allUsers) {
//            for(User otherUser : allUsers) {
//                if(
//                    user.getId() != otherUser.getId() &&
//                    !areComrades(user, otherUser) &&
//                    !alreadyRecruited(user, otherUser) &&
//                    (
//                        containsMatchingGames(user, otherUser) ||
//                        containsMatchingGenres(user, otherUser)
//                    )
//
//                ) {
//                    addRecruits(user, otherUser);
//                }
//            }
//        }
//    }

    private boolean containsMatchingGames(User user1, User user2) {
        Set<Game> user1Games = user1.getProfile().getGames();
        Set<Game> user2Games = user2.getProfile().getGames();
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
        allGenres.addAll(user.getProfile().getGenres());
        for(Game game : user.getProfile().getGames()) {
            for(Genre genre : game.getGenres()) {
                if(!allGenres.contains(genre)) {
                    allGenres.add(genreDao.findById(genre.getId()).get());
                }
            }
        }
        return allGenres;
    }
}
