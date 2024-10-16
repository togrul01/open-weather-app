package org.company.weather.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.company.weather.constants.Constants;
import org.company.weather.dto.WeatherDto;
import org.company.weather.dto.WeatherResponse;
import org.company.weather.entity.WeatherEntity;
import org.company.weather.repository.WeatherRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.cache.annotation.Cacheable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = {"weathers"})
public class WeatherService {
    private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);
    private final WeatherRepository weatherRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Cacheable(key = "#city")
    public WeatherDto getWeatherByCityName(String city) {
        logger.info(("Requested city: " + city));
        Optional<WeatherEntity> weatherOptional = weatherRepository.
                findFirstByRequestedCityNameOrderByUpdatedTimeDesc(city);

        return weatherOptional.map(weather -> {
                    if (weather.getUpdatedTime().isBefore(LocalDateTime.now().minusMinutes(30))) {
                        return WeatherDto.convert(weatherOptional.get());
                    }
                    return WeatherDto.convert(weather);
                })
                .orElseGet(() -> WeatherDto.convert(getWeatherFromWeatherStack(city)));
    }


    private WeatherEntity getWeatherFromWeatherStack(String city) {
        ResponseEntity<String> response = restTemplate.getForEntity(getWeatherStackUrl(city), String.class);
        try {
            WeatherResponse weatherResponse = objectMapper.readValue(response.getBody(), WeatherResponse.class);
            return saveWeather(city, weatherResponse);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @CacheEvict(allEntries = true)
    @PostConstruct
    @Scheduled(fixedRateString = "10000")
    public void clearCache() {
        logger.info("Caches are cleared");
    }

    private String getWeatherStackUrl(String city) {
        return Constants.getApiUrl() +
                Constants.ACCESS_KEY_PARAM +
                Constants.getApiKey() +
                Constants.QUERY_KEY_PARAM
                + city;
    }

    private WeatherEntity saveWeather(String city, WeatherResponse weatherResponse) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        WeatherEntity weather = new WeatherEntity(city,
                weatherResponse.location().name(),
                weatherResponse.location().country(),
                weatherResponse.current().temperature(),
                LocalDateTime.now(),
                LocalDateTime.parse(weatherResponse.location().localtime(), dateTimeFormatter));
        return weatherRepository.save(weather);
    }

}
