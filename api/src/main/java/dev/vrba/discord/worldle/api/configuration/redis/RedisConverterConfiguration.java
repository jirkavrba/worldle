package dev.vrba.discord.worldle.api.configuration.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.convert.RedisCustomConversions;
import org.springframework.lang.NonNull;

import java.util.List;

@Configuration
public class RedisConverterConfiguration {

    @Bean
    public RedisCustomConversions conversions(
            final @NonNull LocalDateToStringConverter dateToStringConverter,
            final @NonNull StringToLocalDateConverter stringToLocalDateConverter
    ) {
        return new RedisCustomConversions(
                List.of(
                        dateToStringConverter,
                        stringToLocalDateConverter
                )
        );
    }
}

