package com.example.demo.service;

import com.example.demo.config.RestTemplateConfig;
import com.example.demo.config.ThreadPoolConfig;
import com.example.demo.model.DataExampleModel;
import com.example.demo.model.OpenWeatherMapModel;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@Configuration
@EnableScheduling
public class MonitorServiceImpl {

    private static Logger logger = Logger.getLogger(MonitorServiceImpl.class.getName());

    private RestTemplateConfig restTemplateConfig;
    private String URL;
    private String apiKey;
    private ThreadPoolConfig threadPoolConfig;

    @Autowired
    public MonitorServiceImpl(RestTemplateConfig restTemplateConfig,
                              @Value("${api.key}") String apiKey,
                              ThreadPoolConfig threadPoolConfig,
                              @Value("${api.url}") String URL) {
        this.restTemplateConfig = restTemplateConfig;
        this.URL = URL;
        this.apiKey = apiKey;
        this.threadPoolConfig = threadPoolConfig;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void monitor() throws IOException, ParseException {
        List<DataExampleModel> dataExampleModels = readFile();
        dataExampleModels.stream().forEach(el -> {
            threadPoolConfig.taskScheduler().scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
                    String url = String.format(URL, el.getCity_name(), apiKey);
                    OpenWeatherMapModel openWeatherMapModel = restTemplateConfig.customRestTemplate().getForObject(url, OpenWeatherMapModel.class);
                    logger.info(timeStamp+", city: "+el.getCity_name()+", temperature: "+openWeatherMapModel.getMain().getTemp()
                            +", wind speed: "+openWeatherMapModel.getWind().getSpeed());
                }
            }, el.getFrequency());
        });
    }

    private List<DataExampleModel> readFile() throws IOException, ParseException {
        List<DataExampleModel> dataExampleModels = new ArrayList<>();
        JSONParser parser = new JSONParser();
        JSONArray jsonParsedArray = (JSONArray) parser.parse(new FileReader("data.json"));

        for (Object obj : jsonParsedArray) {
            JSONObject dataObj = (JSONObject) obj;
            Integer city_id = Integer.valueOf(String.valueOf(dataObj.get("city_id")));
            String city_name = String.valueOf(dataObj.get("city_name"));
            Integer frequency = Integer.valueOf(String.valueOf(dataObj.get("frequency")));
            Integer threshold = Integer.valueOf(String.valueOf(dataObj.get("threshold")));

            DataExampleModel dataExampleModel = DataExampleModel.builder()
                    .city_id(city_id)
                    .city_name(city_name)
                    .frequency(frequency)
                    .threshold(threshold)
                    .build();
            dataExampleModels.add(dataExampleModel);
        }
        return dataExampleModels;
    }
}
