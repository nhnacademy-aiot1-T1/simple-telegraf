package com.nhnacademy.aiotone.measurement;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import lombok.Getter;

@Getter
@Measurement(name = "temperature")
public class Temperature extends BaseMeasurement {

    @Column
    private Double value;
}
