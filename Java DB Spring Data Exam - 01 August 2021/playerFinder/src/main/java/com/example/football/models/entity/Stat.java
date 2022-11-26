package com.example.football.models.entity;


import com.example.football.FootballApplication;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Positive;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "stats")
public class Stat extends AutoId {

    @Column(nullable = false)

    private Float shooting;

    @Column(nullable = false)

    private Float passing;

    @Column(nullable = false)

    private Float endurance;


}
