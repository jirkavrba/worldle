package dev.vrba.discord.worldle.api.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityWebFilterChain filterChain(final @NonNull ServerHttpSecurity http) throws Exception {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .logout(ServerHttpSecurity.LogoutSpec::disable)
                .httpBasic(Customizer.withDefaults())
                .authorizeExchange(exchange ->
                        exchange.pathMatchers("/api/v1/admin/**").hasRole("ADMIN")
                                .anyExchange().permitAll()
                )
                .build();
    }

    @Bean
    public ReactiveUserDetailsService userDetailsManager(
            final @NonNull @Value("${admin.auth.username}") String username,
            final @NonNull @Value("${admin.auth.password}") String password
    ) {
        return new MapReactiveUserDetailsService(
                User.builder()
                        .username(username)
                        .password("{noop}" + password)
                        .roles("ADMIN")
                        .build()
        );
    }
}
