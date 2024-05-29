package com.nhnacademy.aiotone.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.write.Point;
import com.nhnacademy.aiotone.measurement.RawData;
import com.nhnacademy.aiotone.properties.InfluxDbProperties;
import com.nhnacademy.common.aop.CommonLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
@Configuration
@ComponentScan(basePackageClasses = com.nhnacademy.common.config.MessageSenderConfig.class)
public class AppConfig {

    @Bean
    public BlockingQueue<RawData> rawDataBlockingQueue() {
        return new LinkedBlockingQueue<>();
    }

    /**
     * producer - consumer 패턴에서 사용하는 중간 queue.
     */
    @Bean
    public BlockingQueue<List<Point>> pointListBlockingQueue() {
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

    @Bean
    public CommonLogger commonLogger() {
        return new CommonLogger();
    }
}
