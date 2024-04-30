package com.nhnacademy.aiotone.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.nhnacademy.aiotone.measurement.RawData;
import com.nhnacademy.aiotone.mqtt.DefaultMqttSubscriberCallback;
import com.nhnacademy.aiotone.properties.InfluxDbProperties;
import lombok.RequiredArgsConstructor;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Configuration
@RequiredArgsConstructor
@ComponentScan(basePackages = "com.nhnacademy.aiotone.properties")
public class AppConfig {
    private final InfluxDbProperties influxDbProperties;

    /**
     * producer - consumer 패턴에서 사용하는 중간 queue.
     */
    @Bean
    public BlockingQueue<RawData> blockingQueue() {
        return new LinkedBlockingQueue<>();
    }

    @Bean
    public InfluxDBClient influxDBClient() {
        return InfluxDBClientFactory.create(
                influxDbProperties.getUrl(),
                influxDbProperties.getToken().toCharArray(),
                influxDbProperties.getOrg(),
                influxDbProperties.getBucket()
        );
    }

    @Bean
    public MqttCallback mqttCallback(BlockingQueue<RawData> queue, ObjectMapper mapper) {
        return new DefaultMqttSubscriberCallback(queue, mapper);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
