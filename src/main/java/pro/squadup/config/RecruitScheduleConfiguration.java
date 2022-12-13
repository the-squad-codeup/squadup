package pro.squadup.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import pro.squadup.models.*;
import pro.squadup.repositories.ComradeRepository;
import pro.squadup.repositories.GenreRepository;
import pro.squadup.repositories.RecruitRepository;
import pro.squadup.repositories.UserRepository;
import pro.squadup.services.RecruitMatchingService;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
@EnableScheduling
public class RecruitScheduleConfiguration {

    @Autowired
    private RecruitMatchingService recruitMatchingService;

    @Scheduled(fixedRateString = "${recruit.matcher.delay.in.milliseconds}")
    public void runRecruitMatchScheduler() {
        recruitMatchingService.matchAllRecruits();
    }

}
