package com.nhnacademy.aiotone.node.filter.impl;

import com.nhnacademy.aiotone.measurement.BaseMeasurement;
import com.nhnacademy.aiotone.measurement.Temperature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class NullFilterTest {
    BaseMeasurement temperature;

    @BeforeEach
    void setUp() {
        temperature = new Temperature();
    }

    @Test
    @DisplayName("fail 하는 경우를 test 합니다.")
    void isPassedFailCase() {
        assertFalse(NullFilter.getInstance().isPassed(null));
        assertFalse(NullFilter.getInstance().isPassed(temperature));
    }

    @Test
    @DisplayName("pass 하는 경우를 test 합니다.")
    void isPassed() throws Exception {
        Class<?> clazz = temperature.getClass();

        Field f = clazz.getDeclaredField("value");

        f.setAccessible(true);
        f.set(temperature, 20.5);

        assertTrue(NullFilter.getInstance().isPassed(temperature));
    }
}