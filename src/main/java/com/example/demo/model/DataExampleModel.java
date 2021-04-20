package com.example.demo.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class DataExampleModel {
    private Integer city_id;
    private String city_name;
    private Integer frequency;
    private Integer threshold;
}