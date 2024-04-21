package com.nhnacademy.aiotone.node.mqtt;

import com.nhnacademy.aiotone.measurement.BaseMeasurement;
import com.nhnacademy.aiotone.node.BaseNode;
import com.nhnacademy.aiotone.properties.MqttSubscriberProperties;
import com.nhnacademy.aiotone.wire.Wire;
import lombok.Getter;
import org.eclipse.paho.client.mqttv3.*;

import java.util.LinkedList;
import java.util.List;


@Getter
public class MqttSubscriber extends BaseNode {
    private final List<Wire<BaseMeasurement>> list = new LinkedList<>();
    private final MqttSubscriberProperties properties;
    private final IMqttClient subscriber;

    public MqttSubscriber(MqttSubscriberProperties properties) throws MqttException {
        this.properties = properties;
        subscriber = new MqttClient(properties.getUrl(), "mqttSubscriber");

        subscriber.setCallback(new DefaultMqttSubscriberCallback(list));
        subscriber.connect();

        subscriber.subscribe(properties.getTopics().split(","));
    }

    public void wireOut(Wire<BaseMeasurement> wire) {
        list.add(wire);
    }
}
