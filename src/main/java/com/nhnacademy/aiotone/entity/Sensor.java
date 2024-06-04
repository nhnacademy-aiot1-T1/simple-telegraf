package com.nhnacademy.aiotone.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Sensor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "motor_id")
    private Long motorId;

    @Column(name = "channel_no")
    private Integer channelNo;

    @Column(name = "sensor_type")
    private String sensorType;

    @Builder
    public Sensor(Long motorId, Integer channelNo, String sensorType) {
        this.motorId = motorId;
        this.channelNo = channelNo;
        this.sensorType = sensorType;
    }
}
