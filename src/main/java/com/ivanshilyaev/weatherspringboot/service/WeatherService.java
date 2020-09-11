package com.ivanshilyaev.weatherspringboot.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ivanshilyaev.weatherspringboot.bean.WeatherResponse;
import org.springframework.stereotype.Service;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
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

    public WeatherResponse getWeather(String city) throws Exception {
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

        WeatherResponse response = new WeatherResponse();
        double tempInKelvin = Double.parseDouble(mainMap.get("temp").toString());
        response.setTemperature((int) (tempInKelvin - KELVIN_TO_CELSIUS));
        double feelsLike = Double.parseDouble(mainMap.get("feels_like").toString());
        response.setFeelsLike((int) (feelsLike - KELVIN_TO_CELSIUS));
        int pressure = (int) Double.parseDouble(mainMap.get("pressure").toString());
        response.setPressure(pressure);
        int humidity = (int) Double.parseDouble(mainMap.get("humidity").toString());
        response.setHumidity(humidity);

        double visibility = round(Double.parseDouble(responseMap.get("visibility").toString()) / 1000, 1);
        response.setVisibility(visibility);

        List<Map<String, Object>> weather = (List<Map<String, Object>>) (responseMap.get("weather"));
        Map<String, Object> weatherMap = weather.get(0);
        String main = weatherMap.get("main").toString();
        response.setMain(main);
        String description = weatherMap.get("description").toString();
        response.setDescription(description);

        return response;
    }
}
