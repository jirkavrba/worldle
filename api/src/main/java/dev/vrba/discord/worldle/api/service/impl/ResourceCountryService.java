package dev.vrba.discord.worldle.api.service.impl;

import dev.vrba.discord.worldle.api.model.City;
import dev.vrba.discord.worldle.api.model.Country;
import dev.vrba.discord.worldle.api.service.CountryService;
import org.springframework.core.io.ResourceLoader;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ResourceCountryService implements CountryService {

    private final Map<String, Country> countries;

    private final Map<Country, List<City>> cities;

    public ResourceCountryService(final @NonNull ResourceLoader loader) {
        this.countries = loadAndParseCountries(loader);
        this.cities = loadAndParseCities(loader);
    }


    @NonNull
    @Override
    public List<Country> getShuffledCountries() {
        final List<Country> list = new ArrayList<>(countries.values().stream().toList());

        Collections.shuffle(list);

        return list;
    }

    @NonNull
    @Override
    public Optional<City> getRandomCity(final @NonNull Country country) {
        return Optional.of(cities.get(country))
                .filter(collection -> !collection.isEmpty())
                .map(collection -> collection.stream().toList())
                .map(cities -> {
                    final var copy = new ArrayList<>(List.copyOf(cities));

                    Collections.shuffle(copy);
                    return copy.getFirst();
                });
    }

    @NonNull
    private Map<String, Country> loadAndParseCountries(final @NonNull ResourceLoader loader) {
        try {
            return Objects.requireNonNull(loader).getResource("classpath:/countries.csv")
                    .getContentAsString(Charset.defaultCharset())
                    .lines()
                    .map(line -> {
                        // CZ,Czech Republic
                        final String[] parts = line.trim().split(",");
                        final String code = parts[0];
                        final String name = parts[1];
                        final String flag = flagEmoji(code);

                        return new Country(name, code, flag);
                    })
                    .collect(Collectors.toUnmodifiableMap(
                            Country::code,
                            Function.identity()
                    ));
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @NonNull
    private Map<Country, List<City>> loadAndParseCities(final @NonNull ResourceLoader loader) {
        try {
            return Objects.requireNonNull(loader).getResource("classpath:/cities.csv")
                    .getContentAsString(Charset.defaultCharset())
                    .lines()
                    .map(line -> {
                        // Liberec,CZ
                        final String[] parts = line.trim().split(",");
                        final String name = parts[0];
                        final String countryCode = parts[1];
                        final Optional<Country> country = Optional.ofNullable(countries.get(countryCode));

                        return country.map(it -> new City(name, it));
                    })
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.groupingBy(City::country));
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    // CZ -> 🇨🇿
    @NonNull
    private String flagEmoji(final @NonNull String code) {
        final int[] codepoints = code.trim().toUpperCase().chars().map(it -> 127397 + it).toArray();
        return new String(codepoints, 0, codepoints.length);
    }
}
