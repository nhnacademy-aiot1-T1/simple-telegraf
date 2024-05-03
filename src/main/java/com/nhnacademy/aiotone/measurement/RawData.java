package com.nhnacademy.aiotone.measurement;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class RawData {
    private String topic;
    private long time;
    private double[] values;
}
