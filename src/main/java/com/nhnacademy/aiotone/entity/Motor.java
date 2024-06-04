package com.nhnacademy.aiotone.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Cacheable
@Table(name = "motor")
public class Motor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sector_id")
    private Long sectorId = 0L; // default sector id.

    @Column(unique = true, name = "device_name")
    private String deviceName;

    @Column(name = "motor_name")
    private String motorName;

    @Column(columnDefinition = "TINYINT(1)", name = "is_on")
    private Boolean isOn = true;

    @Column(columnDefinition = "TINYINT(1)", name = "is_normal")
    private Boolean isNormal = false;
}
