package org.company.weather.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import org.company.weather.entity.WeatherEntity;

import java.time.LocalDateTime;

public record WeatherDto(
        String cityName,

        String country,
        Integer temperature,
          @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime updatedTime
) {
    public static WeatherDto convert(WeatherEntity weather) {
        return new WeatherDto(
                weather.getCityName(),
                weather.getCountry(),
                weather.getTemperature(),
                weather.getUpdatedTime());
    }
}