package com.nhnacademy.aiotone.node.mqtt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nhnacademy.aiotone.exception.MeasurementNotSupportedException;
import com.nhnacademy.aiotone.measurement.BaseMeasurement;
import com.nhnacademy.aiotone.util.MqttUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MqttUtilsTest {
    String topic;
    String payload;

    @BeforeEach
    void setUp() {
        topic = "data/s/site/b/branch/p/place/d/device/e/humidity";
        payload = "{ \"time\": " + System.currentTimeMillis() + "," + "\"value\": 3 }";
    }


    @Test
    void toMeasurement() throws Exception {
        BaseMeasurement measurement = MqttUtils.toMeasurement(topic, payload);

        assertEquals("device", measurement.getDevice());
        assertEquals(3, measurement.getValue());
    }

    @Test
    @DisplayName("지원하지 않는 measurement를 넣었을 경우 예외를 던지는지 확인하는 테스트 입니다.")
    void toUnSupportMeasurement() throws Exception{
        topic = "data/s/site/b/branch/p/place/d/device/e/empty";

        assertThrows(MeasurementNotSupportedException.class, () -> MqttUtils.toMeasurement(topic, payload));
    }
}