package pro.squadup.utils;

import org.springframework.security.core.context.SecurityContextHolder;
import pro.squadup.models.User;

public class Utils {
    public static Long currentUserId() {
        return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
    }
}
