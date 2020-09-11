package com.ivanshilyaev.weatherspringboot.bean;

import lombok.Data;

@Data
public class WeatherResponse {
    private int temperature; // °C
    private int feelsLike; // °C
    private String main; // Clear
    private String description; // clear sky
    private int pressure; // hPa
    private int humidity; // %
    private double visibility; // km
}
