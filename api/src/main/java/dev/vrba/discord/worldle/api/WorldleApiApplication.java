package dev.vrba.discord.worldle.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@SpringBootApplication
public class WorldleApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(WorldleApiApplication.class, args);
    }

}
