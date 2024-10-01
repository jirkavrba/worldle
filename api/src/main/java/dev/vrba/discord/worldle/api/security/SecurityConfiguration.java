package dev.vrba.discord.worldle.api.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(final @NonNull HttpSecurity http) throws Exception {
        return http
            .csrf(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .logout(AbstractHttpConfigurer::disable)
            .httpBasic(it -> it.realmName("Worldle"))
            .authorizeHttpRequests(requests -> {
                requests.requestMatchers("/api/v1/bot/**").hasRole("BOT");
                requests.anyRequest().permitAll();
            })
            .build();
    }

    @Bean
    public CorsConfigurationSource cors() {
        final CorsConfiguration configuration = new CorsConfiguration();
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "POST"));

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    public UserDetailsManager userDetailsManager(
        final @NonNull @Value("${bot.auth.username}") String username,
        final @NonNull @Value("${bot.auth.password}") String password
    ) {
        return new InMemoryUserDetailsManager(
            User.builder()
                .username(username)
                .password("{noop}" + password)
                .roles("BOT")
                .build()
        );

    }
}
