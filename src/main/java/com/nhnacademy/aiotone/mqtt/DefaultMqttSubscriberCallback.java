package com.nhnacademy.aiotone.mqtt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.aiotone.entity.MotorStatus;
import com.nhnacademy.aiotone.measurement.RawData;
import com.nhnacademy.aiotone.service.MotorStatusService;
import com.nhnacademy.aiotone.util.MqttUtils;
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
    private static final String STATUS_PREFIX = "status";

    private final BlockingQueue<RawData> blockingQueue;
    private final MessageSender messageSender;
    private final ObjectMapper objectMapper;
    private final MotorStatusService motorStatusService;

    @Override
    public void connectionLost(Throwable cause) {
        String message = "mqtt subscriber connection lost";

        messageSender.send(message);
        log.error(message);
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        try {
            String jsonString = MqttUtils.toJsonString(topic, message.toString());

            if (topic.startsWith(STATUS_PREFIX)) {
                MotorStatus status = objectMapper.readValue(jsonString, MotorStatus.class);
                log.warn(jsonString);

                motorStatusService.save(status);

                return;
            }

            RawData rawData = objectMapper.readValue(jsonString, RawData.class);
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
