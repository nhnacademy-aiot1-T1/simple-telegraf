package com.nhnacademy.aiotone.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.aiotone.exception.MeasurementNotSupportedException;
import com.nhnacademy.aiotone.measurement.BaseMeasurement;
import com.nhnacademy.aiotone.measurement.Humidity;
import com.nhnacademy.aiotone.measurement.Temperature;

import java.util.Map;

public class MqttUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Map<String, String> TOPIC_DETAILS = Map.of("s", "site", "b", "branch", "p", "place", "d", "device", "e", "element");
    private static final Map<String, Class<?>> SUPPORT_MEASUREMENT = Map.of("temperature", Temperature.class, "humidity", Humidity.class);
    private static final int MEASUREMENT_INDEX = 10;

    private MqttUtils() {
        throw new IllegalStateException("util 클래스 입니다.");
    }

    /**
     * topic, payload를 json 형태로 변경 후 각각 알맞은 measurement에 대입 후 반환해 주는 함수 입니다,
     */
    public static BaseMeasurement toMeasurement(String topic, String payload) throws JsonProcessingException {
        StringBuilder sb = new StringBuilder();

        String[] topics = topic.split("/");

        sb.append("{ ");
        for (int i = 1; i < topics.length; i += 2) { // topic 형식이 data/topic/../topic/../.. 이런 형식이기 때문에 1부터 시작 인덱스를 잡았습니다, (data/ 부분 스킵)
            sb.append("\"").append(TOPIC_DETAILS.get(topics[i])).append("\"")
                    .append(": ")
                    .append("\"").append(topics[i + 1]).append("\"")
                    .append(",");
        }
        sb.append(payload.replace("{", ""));

        if (!SUPPORT_MEASUREMENT.containsKey(topics[MEASUREMENT_INDEX])) throw new MeasurementNotSupportedException();

        return (BaseMeasurement) objectMapper.readValue(sb.toString(), SUPPORT_MEASUREMENT.get(topics[MEASUREMENT_INDEX]));
    }
}
