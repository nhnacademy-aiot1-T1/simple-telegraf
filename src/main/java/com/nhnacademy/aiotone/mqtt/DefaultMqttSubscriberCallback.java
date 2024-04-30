package com.nhnacademy.aiotone.mqtt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.aiotone.measurement.RawData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.concurrent.BlockingQueue;

@Slf4j
@RequiredArgsConstructor
public class DefaultMqttSubscriberCallback implements MqttCallback {
    private final BlockingQueue<RawData> blockingQueue;
    private final ObjectMapper objectMapper;

    @Override
    public void connectionLost(Throwable cause) {
        log.error(cause.getMessage());
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        try {
            RawData rawData = objectMapper.readValue(message.getPayload(), RawData.class);
            rawData.setTopic(topic);

            blockingQueue.put(rawData);
        }
        catch (Exception e) {
            log.error(
                    "Exception message : {}. \n" +
                    "Input topic : {} \n" +
                    "Payload : {}", e.getMessage(), (topic == null ? "null" : topic), (message.getPayload() == null ? "null" : new String(message.getPayload())));
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
    }
}
