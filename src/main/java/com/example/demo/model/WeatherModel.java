package com.example.demo.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class WeatherModel {
    private Integer id;
    private String main;
    private String description;
    private String icon;
}
