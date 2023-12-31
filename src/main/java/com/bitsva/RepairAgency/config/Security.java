package com.bitsva.RepairAgency.config;

import com.bitsva.RepairAgency.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class Security {
    /*@Autowired
    private UserDetailsService userService;*/

    @Bean
    UserDetailsService userDetailsService() {
        return  new CustomUserDetailsService();
    }

    @Bean
    public static BCryptPasswordEncoder passwordEncoder(){
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
                        request.requestMatchers("/register/**", "/login/**").permitAll()
                                .requestMatchers("/home", "/about", "/contacts").permitAll()
                                .requestMatchers("/users").hasRole("ADMIN")
                                .requestMatchers("/profile/**", "/profileUpdate").hasAnyRole("ADMIN", "CLIENT", "MANAGER", "REPAIRER")
                                .requestMatchers("/balance", "/updateBalance").hasAnyRole("ADMIN", "CLIENT", "MANAGER", "REPAIRER")
                                .requestMatchers("/RepairAgency/requests/**").hasAnyRole("CLIENT", "MANAGER", "REPAIRER")
                                .requestMatchers("/RepairAgency/createRequest", "/RepairAgency/saveRequest", "/RepairAgency/editRequest/**", "/RepairAgency/payForRequest").hasRole("CLIENT")
                                .requestMatchers("/RepairAgency/deleteRequest").hasAnyRole("CLIENT", "MANAGER")
                                .requestMatchers("/RepairAgency/changePaymentStatus", "/RepairAgency/updateCost", "/RepairAgency/updateRepairer").hasRole("MANAGER")
                                .requestMatchers("/RepairAgency/changeCompletionStatus").hasRole("REPAIRER")
                                .requestMatchers("/addFeedback", "/saveFeedback").hasRole("CLIENT")
                                .requestMatchers("/{id}/feedback").hasAnyRole("CLIENT", "MANAGER", "REPAIRER")
                                .requestMatchers("/users/list", "/users/saveUser", "/users/createUser", "/users/editUser", "/users/deleteUser", "/users/changeAccountStatus").hasRole("ADMIN")
                                .requestMatchers("/users/userInfo/**").hasAnyRole("ADMIN", "CLIENT", "MANAGER", "REPAIRER")

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

    /*@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic(Customizer.withDefaults())//BasicAuthenticationFilter
                .csrf(Customizer.withDefaults())
                .authorizeHttpRequests((requests) -> {
                            requests
                                    .requestMatchers("/**")
                                    .permitAll()
                                    .anyRequest()
                                    .authenticated();
                        }
                )
                .csrf(CsrfConfigurer::disable)
                .formLogin(AbstractAuthenticationFilterConfigurer::permitAll)
                .logout(LogoutConfigurer::permitAll);

        return http.build();
    }*/

    /*@Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userService)
                .passwordEncoder(passwordEncoder());
    }*/
}
