package com.nhnacademy.aiotone.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.nhnacademy.aiotone.measurement.RawData;
import com.nhnacademy.aiotone.properties.InfluxDbProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
@Configuration
@ComponentScan(basePackageClasses = com.nhnacademy.common.config.MessageSenderConfig.class)
public class AppConfig {

    /**
     * producer - consumer 패턴에서 사용하는 중간 queue.
     */
    @Bean
    public BlockingQueue<RawData> blockingQueue() {
        return new LinkedBlockingQueue<>();
    }

    @Bean
    public InfluxDBClient influxDBClient(InfluxDbProperties influxDbProperties) {
        return InfluxDBClientFactory.create(
                influxDbProperties.getUrl(),
                influxDbProperties.getToken().toCharArray(),
                influxDbProperties.getOrg(),
                influxDbProperties.getBucket()
        );
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}
