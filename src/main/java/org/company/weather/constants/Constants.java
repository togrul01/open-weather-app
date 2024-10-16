package org.company.weather.constants;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicReference;

@Component
public class Constants {

    private static final AtomicReference<String> API_URL = new AtomicReference<>();
    private static final AtomicReference<String> API_KEY = new AtomicReference<>();

    public static final String ACCESS_KEY_PARAM = "?access_key=";
    public static final String QUERY_KEY_PARAM = "&query=";

    @Value("${weather-stack.api-url}")
    private String apiUrl;

    @Value("${weather-stack.api-key}")
    private String apiKey;

    public static String getApiUrl() {
        return API_URL.get();
    }

    public static void setApiUrl(String apiUrl) {
        API_URL.set(apiUrl);
    }

    public static String getApiKey() {
        return API_KEY.get();
    }

    public static void setApiKey(String apiKey) {
        API_KEY.set(apiKey);
    }

    @PostConstruct
    public void init() {
        Constants.setApiUrl(apiUrl);
        Constants.setApiKey(apiKey);
    }

    private Constants() {
    }
}
