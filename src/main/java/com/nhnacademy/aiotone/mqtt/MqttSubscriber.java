package com.nhnacademy.aiotone.mqtt;

import com.nhnacademy.aiotone.properties.MqttSubscriberProperties;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


@Component
public class MqttSubscriber extends MqttClient {
    private final MqttSubscriberProperties properties;

    public MqttSubscriber(MqttSubscriberProperties properties, MqttCallback callback) throws MqttException {
        super(properties.getUrl(), properties.getClientId());

        this.properties = properties;
        this.setCallback(callback);
    }

    @PostConstruct
    public void start() throws MqttException {
        this.connect();
        this.subscribe(properties.getTopics());
    }
}
