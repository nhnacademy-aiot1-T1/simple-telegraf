package com.nhnacademy.aiotone.node.mqtt;

import com.nhnacademy.aiotone.measurement.BaseMeasurement;
import com.nhnacademy.aiotone.util.MqttUtils;
import com.nhnacademy.aiotone.wire.Wire;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class DefaultMqttSubscriberCallback implements MqttCallback {
    private final List<Wire<BaseMeasurement>> wires;

    @Override
    public void connectionLost(Throwable cause) {
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        try {
            String payload = new String(message.getPayload());

            for (Wire<BaseMeasurement> wire : wires) {
                wire.add(MqttUtils.toMeasurement(topic, payload));
            }
        }
        catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
    }
}
