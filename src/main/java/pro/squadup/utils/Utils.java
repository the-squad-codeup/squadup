package pro.squadup.utils;

import org.springframework.security.core.context.SecurityContextHolder;
import pro.squadup.models.SquadPicture;
import pro.squadup.models.User;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Utils {

    // Returns id of current user
    public static Long currentUserId() {
        return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
    }

    // Generates password reset token string
    public static String generatePasswordResetToken() {
        return  UUID.randomUUID().toString();
    }

    // Generates timestamp of current time plus 10 minutes
    public static Timestamp generatePasswordResetTimestamp() {
        return new Timestamp(new Date().getTime() + TimeUnit.MINUTES.toMillis(10));
    }

    // Creates and returns new default squad picture
    public static SquadPicture defaultSquadPicture() {
        return new SquadPicture
                (
                    "default.png",
                    "dynXYf9AS26ImuqTgUPj",
                    "image/png",
                    47871,
                    "local_file_system",
                    "Stored",
                    "4IRT98MxlthTjGsm",
                    "https://cdn.filestackcontent.com/Humw6OOXTemRtPob8kJB"
                )
        ;
    }
}
