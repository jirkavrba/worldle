package dev.vrba.discord.worldle.api.configuration.redis;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@ReadingConverter
public class StringToLocalDateConverter implements Converter<byte[], LocalDate> {
    @NonNull
    @Override
    public LocalDate convert(@NonNull byte[] source) {
        return LocalDate.parse(new String(source));
    }
}
