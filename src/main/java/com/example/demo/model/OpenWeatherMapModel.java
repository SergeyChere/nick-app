package com.example.demo.model;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class OpenWeatherMapModel {
    private CoordModel coord;
    private List<WeatherModel> weather;
    private String base;
    private MainModel main;
    private Long visibility;
    private WindModel wind;
    private CloudsModel clouds;
    private Long dt;
    private SysModel sys;
    private Integer timezone;
    private Integer id;
    private String name;
    private String cod;
}
