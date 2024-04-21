package com.nhnacademy.aiotone.properties;

import com.nhnacademy.aiotone.util.PropertyUtils;
import lombok.Getter;

import java.util.Properties;

@Getter
public class MqttSubscriberProperties {
    private static final String PREFIX = "mqtt";

    private String url;
    private String topics;

    public MqttSubscriberProperties(Properties properties) {
        PropertyUtils.insertProperties(properties, PREFIX, this);
    }
}
