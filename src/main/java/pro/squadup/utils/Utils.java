package pro.squadup.utils;

import org.springframework.security.core.context.SecurityContextHolder;
import pro.squadup.models.SquadPicture;
import pro.squadup.models.User;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Utils {
    public static Long currentUserId() {
        return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
    }

    public static String generatePasswordResetToken() {
        return  UUID.randomUUID().toString();
    }

    public static Timestamp generatePasswordResetTimestamp() {
        return new Timestamp(new Date().getTime() + TimeUnit.MINUTES.toMillis(10));
    }

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
                    "https://cdn.filestackcontent.com/dynXYf9AS26ImuqTgUPj"
                )
        ;
    }
}
