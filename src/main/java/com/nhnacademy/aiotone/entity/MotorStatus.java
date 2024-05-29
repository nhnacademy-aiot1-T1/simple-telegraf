package com.nhnacademy.aiotone.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Setter
@Table(name = "motor_status")
@JsonIgnoreProperties(ignoreUnknown = true)
public class MotorStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("m")
    @Column(name = "motor_name")
    private String motorName;

    @Column(name = "is_on")
    private Boolean isOn;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}
