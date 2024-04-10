package com.nc13techsolutions.fitnesstrackerbackendserver.config;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.nc13techsolutions.fitnesstrackerbackendserver.models.User;
import com.nc13techsolutions.fitnesstrackerbackendserver.repositories.UserRepo;
import com.nc13techsolutions.fitnesstrackerbackendserver.services.UserService;

@Configuration
@EnableWebSecurity
@PropertySource("application.properties")
public class AuthenticationFilter {
        @Autowired
        private UserService userService;
        @Autowired
        private JwtTokenConfig jwtTokenConfig;
        @Value("${fitness-app-url}")
        private String ALLOWED_URL;

        Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

        @Bean
        public PasswordEncoder encoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public InMemoryUserDetailsManager userDetailsService() {
                InMemoryUserDetailsManager imudm = new InMemoryUserDetailsManager();
                List<User> users = userService.getAllUsers();
                users.forEach((u) -> {
                        User temp = new User(u.getUserId(),
                                        u.getUsername(),
                                        encoder().encode(u.getPassword()),
                                        u.getRole(),
                                        u.getDeviceID(),
                                        u.getDeviceName());
                        imudm.createUser(temp);
                });
                // TODO: This is for creating an admin. Disable once done.
                User default_admin_user = new User(9000,
                                "admin",
                                encoder().encode("password"),
                                UserRepo.ADMIN,
                                "192.168.0.176",
                                "PC:NC13");
                imudm.createUser(default_admin_user);

                return imudm;
        }

        @Bean
        public AuthenticationProvider authenticationProvider() {
                DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
                daoAuthenticationProvider.setUserDetailsService(userDetailsService());
                daoAuthenticationProvider.setPasswordEncoder(encoder());

                return daoAuthenticationProvider;
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
                return configuration.getAuthenticationManager();
        }

        public UrlBasedCorsConfigurationSource corsConfigurationSource() {
                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowCredentials(true);
                configuration.addAllowedOrigin(ALLOWED_URL);
                configuration.addAllowedHeader("*");
                configuration.addAllowedMethod("*");
                source.registerCorsConfiguration("/**", configuration);
                return source;
        }

        @Bean
        @Order(1)
        public SecurityFilterChain noSecurityFilterChain(HttpSecurity http) throws Exception {
                // TODO: End points here will be used without any authentication
                String[] zeroSecurityEndpoints = { "/api/user/login", "/api/user/validate" };
                // This only checks endpoints that are available to all
                http.securityMatcher(zeroSecurityEndpoints);
                http.authorizeHttpRequests(
                                (authorizeHttpRequests) -> authorizeHttpRequests
                                                .requestMatchers(zeroSecurityEndpoints).permitAll())
                                .csrf((csrf) -> csrf
                                                .disable())
                                .cors((cors) -> {
                                        cors.configurationSource(corsConfigurationSource());
                                })
                                .httpBasic((h) -> {
                                });
                return http.build();
        }

        @Bean
        @Order(2)
        public SecurityFilterChain commonSecurityFilterChain(HttpSecurity http) throws Exception {
                // TODO: Remember to add end points that'll used by both user and admin here
                String[] commonEndpoints = { "/exercise/**", "/files/add", "/files/images/**",
                                "/files/videos/**, /daydata/**, /schedule/**", "/files/images/view/**",
                                "/files/videos/view/**" };
                // This only checks endpoints that are accessible to both user and admin
                http.securityMatcher(commonEndpoints);
                http.authorizeHttpRequests(
                                (authorizeHttpRequests) -> authorizeHttpRequests
                                                .requestMatchers(commonEndpoints)
                                                .hasAnyRole(UserRepo.ADMIN, UserRepo.USER))
                                .sessionManagement((customizer) -> {
                                        customizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                                })
                                .authenticationProvider(authenticationProvider())
                                .addFilterBefore(jwtTokenConfig, UsernamePasswordAuthenticationFilter.class)
                                .csrf((csrf) -> csrf
                                                .disable())
                                .cors((cors) -> {
                                        cors.configurationSource(corsConfigurationSource());
                                })
                                .httpBasic((h) -> {
                                });
                return http.build();
        }

        @Bean
        @Order(3)
        public SecurityFilterChain adminSecurityFilterChain(HttpSecurity http) throws Exception {
                // TODO: Remember to add end points used only by admin here
                String[] adminEndpoints = { "/api/user/**" };
                // This only checks endpoints that are accessible admin
                http.securityMatcher(adminEndpoints);
                http.authorizeHttpRequests(
                                (authorizeHttpRequests) -> authorizeHttpRequests
                                                .requestMatchers(adminEndpoints).hasRole(UserRepo.ADMIN))
                                .sessionManagement((customizer) -> {
                                        customizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                                })
                                .authenticationProvider(authenticationProvider())
                                .addFilterBefore(jwtTokenConfig, UsernamePasswordAuthenticationFilter.class)
                                .csrf((csrf) -> csrf
                                                .disable())
                                .cors((cors) -> {
                                        cors.configurationSource(corsConfigurationSource());
                                })
                                .httpBasic((h) -> {
                                });
                return http.build();
        }

}
