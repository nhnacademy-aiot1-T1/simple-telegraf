package com.nhnacademy.aiotone.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "influxdb")
public class InfluxDbProperties {
    private String url;
    private String token;
    private String org;
    private String bucket;
}
