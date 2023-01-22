package pro.squadup.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

    // Encodes passwords
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Security filter chain to redirect successful login/logouts. Dictates which views require authentication
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                // Login configuration
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/hq")
                .permitAll()
                // Logout configuration
                .and()
                .logout()
                .logoutSuccessUrl("/login?logout")
                // Pages only viewable when logged in
                .and()
                .authorizeRequests()
                .antMatchers(
                        "/dashboard",
                        "/comrades",
                        "/recruits",
                        "/profile",
                        "/build-profile",
                        "/games",
                        "/profile/*",
                        "/profile/*/*",
                        "/game/*",
                        "/squad/*",
                        "/squads",
                        "/squads/*",
                        "/squads/*/*",
                        "/invites",
                        "/invites/*",
                        "/invites/*/*",
                        "/messages",
                        "/messages/*",
                        "/messages/*/*",
                        "/messages/*/*/*",
                        "/hq",
                        "/hq/*",
                        "/hq/*/*"
                )
                .authenticated()
                // Pages viewable without logging in
                .and()
                .authorizeRequests()
                .antMatchers(
                        "/",
                        "/contact",
                        "/about"
                )
                .permitAll()
        ;
        return http.build();
    }

}
