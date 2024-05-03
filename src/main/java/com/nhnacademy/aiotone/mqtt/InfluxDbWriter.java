package com.nhnacademy.aiotone.mqtt;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.influxdb.exceptions.InfluxException;
import com.nhnacademy.aiotone.measurement.RawData;
import com.nhnacademy.common.notification.MessageSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

@Slf4j
@Component
@RequiredArgsConstructor
public class InfluxDbWriter {
    private static final int BATCH_SIZE = 1024;
    private static final int THREAD_COUNT = 2;

    private final ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
    private final MessageSender messageSender;
    private final InfluxDBClient influxDBClient;
    private final BlockingQueue<RawData> blockingQueue;

    @PostConstruct
    public void start() {
        IntStream.range(0, THREAD_COUNT)
                .forEach(i -> executorService.execute(() -> {

                            List<Point> points = new ArrayList<>(BATCH_SIZE * 2);
                            try {
                                while (!Thread.currentThread().isInterrupted()) {
                                    RawData raw = blockingQueue.take();

                                    String[] topics = raw.getTopic().split("/");
                                    Instant time = Instant.ofEpochMilli(raw.getTime());
                                    long intervalMilli = 1_000 / raw.getValues().length;

                                    for (int j = 0; j < raw.getValues().length; ++j) {
                                        Point point = Point.measurement("measurement")
                                                .addTag("domain", topics[2])
                                                .addTag("factory", topics[4])
                                                .addTag("gateway", topics[6])
                                                .addTag("channel", topics[8])
                                                .addField("value", raw.getValues()[j])
                                                .time(time, WritePrecision.MS);

                                        time = time.plusMillis(intervalMilli);
                                        points.add(point);
                                    }

                                    if (points.size() >= BATCH_SIZE) {
                                        influxDBClient.getWriteApiBlocking().writePoints(points);
                                        log.info("Write Data point into specified bucket.");

                                        points.clear();
                                    }
                                }

                            } catch (InfluxException influxException) {
                                if (!influxDBClient.ping()) {
                                    messageSender.send("influx DB 서버를 확인해주세요");
                                    log.error(influxException.getMessage());

                                    try {
                                        Thread.currentThread().wait(180_000);
                                    } catch (InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }
                                }

                                /**
                                 * influx db 서버가 동작 중일 경우, 다시 한번 write를 시도합니다.
                                 */
                                if (influxDBClient.ping()) {
                                    try {
                                        influxDBClient.getWriteApiBlocking().writePoints(points);
                                    } catch (InfluxException ignore) {
                                        log.error(influxException.getMessage());
                                    }
                                }

                            } catch (InterruptedException e) {
                                messageSender.send("Data를 가지고 오는 과정에서 문제가 생겨 thread가 종료되었습니다 : " + e.getMessage());
                                log.error(e.getMessage());
                            }
                        })
                );
    }
}
