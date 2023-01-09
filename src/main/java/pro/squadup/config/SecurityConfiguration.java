package pro.squadup.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                // Login configuration
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/dashboard")
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
                        "/game/*",
                        "/squad/*"
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
