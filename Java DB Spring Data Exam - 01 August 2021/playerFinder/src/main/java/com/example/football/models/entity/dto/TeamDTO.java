package com.example.football.models.entity.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class TeamDTO {

    @Size(min = 3)
    private String name;

    @Size(min = 3)
    private String stadiumName;

    @Min(1000)
    private Integer fanBase;

    @Size(min = 10)
    private String history;

    private String townName;

}
