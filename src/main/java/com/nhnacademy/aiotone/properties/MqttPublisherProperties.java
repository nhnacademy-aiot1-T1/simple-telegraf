package com.nhnacademy.aiotone.properties;

import com.nhnacademy.aiotone.util.PropertyUtils;
import lombok.Getter;

import java.util.Properties;

@Getter
public class MqttPublisherProperties {
    private static final String PREFIX = "influxdb";

    private String url;
    private String token;
    private String org;
    private String bucket;

    public MqttPublisherProperties(Properties properties) {
        PropertyUtils.insertProperties(properties, PREFIX, this);
    }
}
