package org.company.weather.repository;

import org.company.weather.entity.WeatherEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WeatherRepository extends JpaRepository<WeatherEntity, String> {

    Optional<WeatherEntity> findFirstByRequestedCityNameOrderByUpdatedTimeDesc(String s); // en son kaydedilen datani getirir
}
