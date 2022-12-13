package pro.squadup.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import pro.squadup.repositories.UserRepository;

@Configuration
@EnableScheduling
public class RecruitScheduleConfiguration {

    private UserRepository userDao;

    public RecruitScheduleConfiguration(UserRepository userDao) {
        this.userDao = userDao;
    }
}
