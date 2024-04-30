package com.nhnacademy.aiotone.mqtt;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.influxdb.exceptions.InfluxException;
import com.nhnacademy.aiotone.measurement.RawData;
import com.nhnacademy.common.notification.impl.DoorayMessageSenderImpl;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

@Getter
@Slf4j
@Component
public class MqttPublisher {
    private static final int BATCH_SIZE = 256;
    private static final int THREAD_COUNT = 2;

    private final InfluxDBClient influxDBClient;
    private final ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);

    private final BlockingQueue<RawData> blockingQueue;

    public MqttPublisher(InfluxDBClient influxDBClient, BlockingQueue<RawData> blockingQueue) {
        this.influxDBClient = influxDBClient;
        this.blockingQueue = blockingQueue;

        IntStream.range(0, THREAD_COUNT)
                .forEach(i -> executorService.execute(() -> {

                            List<Point> points = new ArrayList<>(BATCH_SIZE);

                            try {
                                while (!Thread.currentThread().isInterrupted()) {
                                    RawData raw = blockingQueue.take();
                                    long start = System.currentTimeMillis();
                                    points.clear();

                                    String[] topics = raw.getTopic().split("/");
                                    Instant time = Instant.ofEpochMilli(raw.getTime());
                                    long interval = raw.getTime() / raw.getValues().length;

                                    for (int j = 0; j < raw.getValues().length; ++j) {
                                        Point point = Point.measurement("measurement")
                                                .addTag("domain", topics[2])
                                                .addTag("factory", topics[4])
                                                .addTag("gateway", topics[6])
                                                .addTag("channel", topics[8])
                                                .addField("value", raw.getValues()[j])
                                                .time(time, WritePrecision.MS);

                                        time = time.plusMillis(interval);
                                        points.add(point);
                                    }


                                    influxDBClient.getWriteApiBlocking().writePoints(points);
                                    long end = System.currentTimeMillis();

                                    log.info("{} 밀리초가 경과 되었습니다.", end - start);
                                }

                            } catch (InfluxException influxException) {
                                if (!influxDBClient.ping()) {
                                    Thread.currentThread().interrupt();

                                    new DoorayMessageSenderImpl().send("influx DB 서버를 확인해주세요");
                                }

                                while (influxDBClient.ping()) {
                                    try {
                                        influxDBClient.getWriteApiBlocking().writePoints(points);
                                        break;
                                    } catch (InfluxException ignore) {
                                        log.error(influxException.getMessage());
                                    }
                                }

                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                                log.error(e.getMessage());
                            }
                        })
                );
    }
}

