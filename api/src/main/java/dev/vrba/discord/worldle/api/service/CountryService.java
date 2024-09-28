package dev.vrba.discord.worldle.api.service;

import dev.vrba.discord.worldle.api.model.City;
import dev.vrba.discord.worldle.api.model.Country;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface CountryService {

    @NonNull
    List<Country> getAvailableCountries();

    @NonNull
    Optional<City> getRandomCity(@NonNull Country country);
}
