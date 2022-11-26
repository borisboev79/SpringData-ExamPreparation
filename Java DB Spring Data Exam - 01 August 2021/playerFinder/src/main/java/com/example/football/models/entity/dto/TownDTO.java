package com.example.football.models.entity.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;


@Getter
@NoArgsConstructor
public class TownDTO {

    @Size(min = 2)
    private String name;

    @Positive
    private Integer population;

    @Size(min = 10)
    private String travelGuide;

}
