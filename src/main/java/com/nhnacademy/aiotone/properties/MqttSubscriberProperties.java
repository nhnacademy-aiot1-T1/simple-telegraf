package com.nhnacademy.aiotone.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "mqtt")
public class MqttSubscriberProperties {
    private String url;
    private String topics;
    private String clientId;
}
