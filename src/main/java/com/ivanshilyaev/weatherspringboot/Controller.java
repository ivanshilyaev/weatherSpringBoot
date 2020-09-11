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

    @Autowired
    private WeatherService service;

    private static List<String> cities = new ArrayList<>(Arrays.asList(new String[]{"Brest", "Vitebsk", "Gomel", "Grodno", "Mogilev", "Minsk"}));

    @GetMapping("/main")
    public String main(Model model) {
        model.addAttribute("cities", cities);
        return "main";
    }

    @GetMapping("/weather")
    public String weather(@ModelAttribute("city") String city, Model model) throws Exception {
        WeatherResponse response = service.getWeather(city);
        model.addAttribute("city", city);
        model.addAttribute("response", response);
        return "weather";
    }
}
