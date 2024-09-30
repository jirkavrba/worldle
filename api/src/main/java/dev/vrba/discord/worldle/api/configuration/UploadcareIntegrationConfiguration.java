package dev.vrba.discord.worldle.api.configuration;

import com.uploadcare.api.Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;

@Configuration
public class UploadcareIntegrationConfiguration {

    @Bean
    public Client uploadCareApiClient(
            final @NonNull @Value("${integration.uploadcare.public-key}") String publicKey,
            final @NonNull @Value("${integration.uploadcare.secret-key}") String secretKey
    ) {
        return new Client(publicKey, secretKey);
    }
}
