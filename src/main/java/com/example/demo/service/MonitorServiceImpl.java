package com.example.demo.service;

import com.example.demo.config.RestTemplateConfig;
import com.example.demo.model.OpenWeatherMapModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@Configuration
@EnableScheduling
public class MonitorServiceImpl {

    private static Logger logger = Logger.getLogger(MonitorServiceImpl.class.getName());

    private RestTemplateConfig restTemplateConfig;
    private List<String> cities;
    private String URL;
    private String apiKey;

    @Autowired
    public MonitorServiceImpl(RestTemplateConfig restTemplateConfig,
                              @Value("${cities.names}") List<String> cities,
                              @Value("${api.key}") String apiKey,
                              @Value("${api.url}") String URL) {
        this.restTemplateConfig = restTemplateConfig;
        this.cities = cities;
        this.URL = URL;
        this.apiKey = apiKey;
    }

    @Scheduled(fixedDelayString  = "${monitored.weather.frequency}")
    public void scheduleFixedDelayTask() {
        for (String city : cities) {
            String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
            String url = String.format(URL, city, apiKey);
            OpenWeatherMapModel openWeatherMapModel = restTemplateConfig.customRestTemplate().getForObject(url, OpenWeatherMapModel.class);
            logger.info(timeStamp+", city: "+city+", temperature: "+openWeatherMapModel.getMain().getTemp()
                    +", wind speed: "+openWeatherMapModel.getWind().getSpeed());
        }
    }
}
