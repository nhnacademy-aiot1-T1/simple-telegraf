package com.nhnacademy.aiotone.measurement;

import com.influxdb.annotations.Column;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
public abstract class BaseMeasurement {

    protected BaseMeasurement() {
    }

    @Column(tag = true)
    private String site;

    @Column(tag = true)
    private String branch;

    @Column(tag = true)
    private String place;

    @Column(tag = true)
    private String device;

    @Column(tag = true)
    private String element;

    private long time; // to instant.

    @Setter
    @Column(timestamp = true)
    private Instant instant;

    public abstract Object getValue();

    public void setTimestamp() {
        instant = Instant.ofEpochMilli(time);
    }
}
