package com.nhnacademy.aiotone.mqtt;

import com.nhnacademy.aiotone.properties.MqttSubscriberProperties;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.stereotype.Component;


@Getter
@Slf4j
@Component
public class MqttSubscriber extends MqttClient {
    private final MqttSubscriberProperties properties;

    public MqttSubscriber(MqttSubscriberProperties properties, MqttCallback callback) throws MqttException {
        super(properties.getUrl(), properties.getClientId());

        this.properties = properties;
        this.setCallback(callback);

        this.connect();
        this.subscribe(properties.getTopics().split(","));
    }
}
