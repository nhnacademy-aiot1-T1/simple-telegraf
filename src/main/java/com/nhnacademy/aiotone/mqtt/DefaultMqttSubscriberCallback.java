package com.nhnacademy.aiotone.mqtt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.aiotone.measurement.RawData;
import com.nhnacademy.common.notification.MessageSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;

@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultMqttSubscriberCallback implements MqttCallback {
    private final BlockingQueue<RawData> blockingQueue;
    private final MessageSender messageSender;
    private final ObjectMapper objectMapper;

    @Override
    public void connectionLost(Throwable cause) {
        messageSender.send("mqtt subscriber connection lost : " + cause.getMessage());

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
                    "Exception message : {}. Input topic : {} Payload : {}",
                    e.getMessage(),
                    (topic == null ? "null" : topic),
                    (message.getPayload() == null ? "null" : new String(message.getPayload())));
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
    }
}
