package com.nhnacademy.aiotone.converter.impl;

import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.nhnacademy.aiotone.converter.Converter;
import com.nhnacademy.aiotone.measurement.RawData;
import com.nhnacademy.aiotone.properties.MotorChannelInfoProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Slf4j
@Component
public class DefaultRawToPointListConverter implements Converter<RawData, List<Point>> {
    private final BlockingQueue<RawData> inputQueue;
    private final BlockingQueue<List<Point>> outQueue;
    private final MotorChannelInfoProperties motorChannelInfoProperties;

    public List<Point> convert(RawData from) {
        long time = TimeUnit.MILLISECONDS.toNanos(from.getTime());
        long intervalNanoSec = TimeUnit.SECONDS.toNanos(1L) / from.getValues().length;

        List<Point> points = new ArrayList<>(from.getValues().length);
        String channelSensor = motorChannelInfoProperties.getChannelSensor(from.getChannel());

        Double[] values = from.getValues();

        for (int i = 0; i < from.getValues().length; ++i) {
            Point point = Point.measurement("rawData")
                    .addTag("gateway", from.getGateway())
                    .addTag("motor", from.getMotor())
                    .addTag("channel", channelSensor)
                    .addField("value", values[i])
                    .time(time, WritePrecision.NS);

            time += intervalNanoSec;
            points.add(point);
        }

        return points;
    }

    @PostConstruct
    public void start() {
        Thread converter = new Thread(() -> {
            while(!Thread.currentThread().isInterrupted()) {
                try {
                    RawData rawData = inputQueue.take();
                    log.info("take raw data.");

                    List<Point> convertedData = convert(rawData);
                    log.info("points size : {}", convertedData.size());

                    outQueue.put(convertedData);
                    log.info("put converted data.");

                } catch (InterruptedException e) {
                    log.error(e.getMessage());
                    throw new IllegalStateException(e.getMessage());
                }
            }
        });

        converter.start();
    }
}
