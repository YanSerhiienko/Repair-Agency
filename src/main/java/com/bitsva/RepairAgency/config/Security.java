package com.bitsva.RepairAgency.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class Security {
    @Bean
    UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }

    @Bean
    public static BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(Customizer.withDefaults())
                .authorizeHttpRequests((request) ->
                        request
                                .requestMatchers("/css/**", "/js/**", "/img/**").permitAll()
                                .requestMatchers("/register/**", "/login/**").permitAll()
                                .requestMatchers("/home", "/about", "/contacts").permitAll()
                                .requestMatchers("/profile/**", "/profileUpdate").hasAnyRole("SUPER_ADMIN", "ADMIN", "CLIENT", "MANAGER", "REPAIRER")
                                .requestMatchers("/balance", "/updateBalance").hasAnyRole("SUPER_ADMIN", "ADMIN", "CLIENT", "MANAGER", "REPAIRER")
                                .requestMatchers("/requests/list/**").hasAnyRole("CLIENT", "MANAGER", "REPAIRER")
                                .requestMatchers("/requests/createRequest", "/requests/saveRequest", "/requests/editRequest/**", "/requests/payForRequest").hasRole("CLIENT")
                                .requestMatchers("/requests/deleteRequest").hasAnyRole("CLIENT", "MANAGER")
                                .requestMatchers("/requests/changePaymentStatus", "/requests/updateCost", "/requests/updateRepairer").hasRole("MANAGER")
                                .requestMatchers("/requests/changeCompletionStatus").hasRole("REPAIRER")
                                .requestMatchers("/addFeedback", "/saveFeedback").hasRole("CLIENT")
                                .requestMatchers("/requests/{id}/feedback").hasAnyRole("CLIENT", "MANAGER", "REPAIRER")
                                .requestMatchers("/users/list", "/users/page/*", "/users/saveUser", "/users/updateUser", "/users/createUser", "/users/editUser", "/users/deleteUser", "/users/changeAccountStatus").hasAnyRole("SUPER_ADMIN", "ADMIN")
                                .requestMatchers("/users/userInfo/**").hasAnyRole("SUPER_ADMIN", "ADMIN", "CLIENT", "MANAGER", "REPAIRER")
                )
                .csrf(CsrfConfigurer::disable)
                .formLogin(
                        form -> form
                                .loginPage("/login")
                                .loginProcessingUrl("/login")
                                .defaultSuccessUrl("/home")
                                .permitAll()
                ).logout(
                        logout -> logout
                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                .permitAll()
                );
        return http.build();
    }
}
