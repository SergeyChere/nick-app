package com.example.demo.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class SysModel {
    private Integer type;
    private Integer id;
    private String country;
    private Integer sunrise;
    private Integer sunset;
}
