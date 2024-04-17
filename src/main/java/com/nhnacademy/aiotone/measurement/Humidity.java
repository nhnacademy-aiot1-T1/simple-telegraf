package com.nhnacademy.aiotone.measurement;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import lombok.Getter;

@Getter
@Measurement(name = "humidity")
public class Humidity extends BaseMeasurement {

    @Column
    private Integer value;
}
