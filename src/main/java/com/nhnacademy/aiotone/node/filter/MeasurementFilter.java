package com.nhnacademy.aiotone.node.filter;

import com.nhnacademy.aiotone.measurement.BaseMeasurement;

public interface MeasurementFilter {

    boolean isPassed(BaseMeasurement measurement);
}
