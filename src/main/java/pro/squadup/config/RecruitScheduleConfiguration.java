package pro.squadup.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import pro.squadup.services.RecruitMatchingService;

@Configuration
@EnableScheduling
public class RecruitScheduleConfiguration {

    @Autowired
    private RecruitMatchingService recruitMatchingService;

    // Runs recruit matching algorithm to every user every night at midnight
    @Scheduled(cron = "0 0 0 * * ?")
    public void runRecruitMatchScheduler() throws JsonProcessingException {
        recruitMatchingService.matchAllRecruits();
    }

}
