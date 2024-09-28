package dev.vrba.discord.worldle.api.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;

import java.time.Clock;

@Configuration
public class ClockConfiguration {

    @Bean
    @NonNull
    public Clock clock() {
        return Clock.systemDefaultZone();
    }

}
