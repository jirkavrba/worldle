package dev.vrba.discord.worldle.api.service;

import dev.vrba.discord.worldle.api.model.City;
import dev.vrba.discord.worldle.api.model.Country;
import org.springframework.lang.NonNull;

import java.util.List;

public interface CountryService {

    @NonNull
    List<Country> getAvailableCountries();

    @NonNull
    City getRandomCity(@NonNull Country country);
}
