package org.medilabo.ui.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    /**
     * Defines the password hash algorithm.
     *
     * @return A new instance of {@link BCryptPasswordEncoder}
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures the security filter chain.
     *
     * @param http the security configurator object
     * @return the configured security filter chain
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/css/**").permitAll()
                        .requestMatchers("/patient/list", "/patient/add", "/patient/update/**", "/patient/validate").hasAnyRole("ORGANIZER", "DOCTOR")
                        .requestMatchers("/patient/details/**", "/note/**").hasRole("DOCTOR")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .defaultSuccessUrl("/patient/list", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .exceptionHandling(ex -> ex
                        .accessDeniedPage("/403")
                )
                .userDetailsService(userDetailsService(passwordEncoder()));
        return http.build();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails organizer = User.builder()
                .username("medilabo.org")
                .password(passwordEncoder.encode("Password123"))
                .roles("ORGANIZER")
                .build();

        UserDetails doctor = User.builder()
                .username("medilabo.doc")
                .password(passwordEncoder.encode("DocMedilab123%"))
                .roles("DOCTOR")
                .build();

        return new InMemoryUserDetailsManager(organizer, doctor);
    }
}
