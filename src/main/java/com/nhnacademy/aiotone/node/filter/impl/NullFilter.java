package com.nhnacademy.aiotone.node.filter.impl;

import com.nhnacademy.aiotone.measurement.BaseMeasurement;
import com.nhnacademy.aiotone.node.filter.MeasurementFilter;
import lombok.Getter;

/**
 * null이 들어오거나, time, value의 값이 null일 경우 false를 반환합니다.
 */
public class NullFilter implements MeasurementFilter {
    @Getter
    private static NullFilter instance;

    private NullFilter() {
    }

    @Override
    public boolean isPassed(BaseMeasurement measurement) {
        return (measurement != null && measurement.getValue() != null);
    }

    static {
        instance = new NullFilter();
    }
}
