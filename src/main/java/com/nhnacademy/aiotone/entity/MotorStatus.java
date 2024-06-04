package com.nhnacademy.aiotone.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Table(name = "motor_running_log")
@JsonIgnoreProperties(ignoreUnknown = true)
public class MotorStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Transient
    @JsonProperty("m")
    private String motorName;

    @Column(name = "motor_id")
    private Long motorId;

    @Column(columnDefinition = "TINYINT(1)", name = "is_on")
    private Boolean isOn;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}
