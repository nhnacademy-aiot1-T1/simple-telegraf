package com.nhnacademy.aiotone.measurement;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class RawData {
    private String topic;
    private long time;
    private double[] values;
}
