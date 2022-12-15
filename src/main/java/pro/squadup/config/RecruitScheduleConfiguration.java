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

    @Scheduled(fixedRateString = "${recruit.matcher.delay.in.milliseconds}")
    public void runRecruitMatchScheduler() throws JsonProcessingException {
        recruitMatchingService.matchAllRecruits();
    }

}
