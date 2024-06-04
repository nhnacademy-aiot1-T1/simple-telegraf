package com.nhnacademy.aiotone.properties;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Setter
@Component
@ConfigurationProperties(prefix = "motor")
public class MotorChannelInfoProperties {
    private String channel0;
    private String channel1;
    private String channel2;
    private String channel3;
    private String channel4;
    private String channel5;
    private String channel6;
    private String channel7;
    private String channel8;
    private String channel9;

    public String getChannelSensor(int num) {
        try {
            Field field = this.getClass().getDeclaredField("channel" + num);

            return field.get(this).toString();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalArgumentException();
        }
    }
}
