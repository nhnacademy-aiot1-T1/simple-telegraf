package com.nhnacademy.aiotone.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.aiotone.measurement.RawData;
import lombok.extern.slf4j.Slf4j;

import java.util.stream.IntStream;

@Slf4j
public class MqttUtils {
    private MqttUtils() {
        throw new IllegalStateException("util class 입니다.");
    }

    public static String toJsonString(String topic, String payload) {
        String[] split = topic.split("/");
        StringBuilder parsedTopic = new StringBuilder();

        for (int i = 1; i < split.length; i += 2) {
            parsedTopic.append(String.format("\"%s\": \"%s\", ", split[i], split[i + 1]));
        }

        String res = "{" + parsedTopic + payload.replace('{', ' ');
        log.info(res);

        return res;
    }
}
