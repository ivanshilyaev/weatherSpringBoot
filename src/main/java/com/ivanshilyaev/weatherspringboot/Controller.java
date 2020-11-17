package com.ivanshilyaev.weatherspringboot;

import com.ivanshilyaev.weatherspringboot.bean.WeatherResponse;
import com.ivanshilyaev.weatherspringboot.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@org.springframework.stereotype.Controller
public class Controller {

    private final WeatherService service;

    private static final List<String> cities = new ArrayList<>(Arrays.asList("Brest", "Vitebsk", "Gomel", "Grodno", "Mogilev", "Minsk"));

    @Autowired
    public Controller(WeatherService service) {
        this.service = service;
    }

    @GetMapping("/main")
    public String main(Model model) {
        model.addAttribute("cities", cities);
        return "main";
    }

    @GetMapping("/weather")
    public String weather(Model model, @ModelAttribute("city") String city) throws Exception {
        WeatherResponse response = service.getWeather(city);
        model.addAttribute("city", city);
        model.addAttribute("response", response);
        return "weather";
    }
}
