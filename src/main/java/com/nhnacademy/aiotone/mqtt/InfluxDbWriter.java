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
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@Slf4j
@Component
@RequiredArgsConstructor
public class InfluxDbWriter {
    private static final int RETRY_INTERVAL_MILLI = 30_000; // 30초.
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
                                    long intervalNanoSec = TimeUnit.SECONDS.toNanos(1) / raw.getValues().length;

                                    for (double value : raw.getValues()) {
                                        Point point = Point.measurement("rawData")
                                                .addTag("gateway", topics[6])
                                                .addTag("motor", topics[8])
                                                .addTag("channel", topics[10])
                                                .addField("value", value)
                                                .time(time, WritePrecision.NS);

                                        time = time.plusMillis(intervalNanoSec);
                                        points.add(point);
                                    }

                                    if (points.size() >= BATCH_SIZE) {
                                        influxDBClient.getWriteApiBlocking().writePoints(points);
                                        log.info("Write Data point into specified bucket.");

                                        points.clear();
                                    }
                                }

                            } catch (InfluxException influxException) {

                                // influx db 서버가 동작 중일 경우, 30초 대기 후 다시 write를 시도합니다, 만약 30초 대기 후에도 ping이 false일 경우 알림을 주고 쓰레드를 종료 합니다.
                                if (!influxDBClient.ping()) {

                                    try {
                                        Thread.sleep(RETRY_INTERVAL_MILLI);

                                    } catch (InterruptedException e) {
                                        log.error(e.getMessage());
                                        Thread.currentThread().interrupt();
                                    }
                                }

                                if (influxDBClient.ping()) {
                                    try {
                                        influxDBClient.getWriteApiBlocking().writePoints(points);
                                    } catch (InfluxException ignore) {
                                        log.error("다시 DB에 write를 시도 했으나 실패했습니다 : {}", influxException.getMessage());
                                    }
                                }
                                else {
                                    messageSender.send("influx db 서버를 확인해주세요.");
                                    Thread.currentThread().interrupt();
                                }

                            } catch (InterruptedException e) {
                                log.error(e.getMessage());
                                Thread.currentThread().interrupt();
                            }
                        })
                );
    }
}
