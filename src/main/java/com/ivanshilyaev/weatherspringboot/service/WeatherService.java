package com.ivanshilyaev.weatherspringboot.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Service;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

@Service
public class WeatherService {
    private static final String API_KEY = "f02c5f07c8d826c5de303e4b6d365768";
    private static final double KELVIN_TO_CELSIUS = 273.15;

    private Map<String, Object> jsonToMap(String line) {
        return new Gson().fromJson(line, new TypeToken<HashMap<String, Object>>() {
        }.getType());
    }

    private double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public String getWeather(String city) throws Exception {
        String urlString = "http://api.openweathermap.org/data/2.5/weather?q=" +
                city + "&appid=" + API_KEY;
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();
        int responseCode = connection.getResponseCode();
        StringBuilder result = new StringBuilder();
        if (responseCode != 200) {
            throw new RuntimeException("HttpResponseCode: " + responseCode);
        } else {
            Scanner scanner = new Scanner(url.openStream());
            while (scanner.hasNext()) {
                result.append(scanner.nextLine());
            }
            scanner.close();
        }
        Map<String, Object> responseMap = jsonToMap(result.toString());
        Map<String, Object> mainMap = jsonToMap(responseMap.get("main").toString());
        double tempInKelvin = Double.parseDouble(mainMap.get("temp").toString());
        return round(tempInKelvin - KELVIN_TO_CELSIUS, 2) + " °C";
    }
}
