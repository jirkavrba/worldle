package dev.vrba.discord.worldle.api.configuration.redis;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

@Component
@WritingConverter
public class LocalDateToStringConverter implements Converter<LocalDate, byte[]> {
    @NonNull
    @Override
    public byte[] convert(@NonNull LocalDate source) {
        return source.toString().getBytes(StandardCharsets.UTF_8);
    }
}
