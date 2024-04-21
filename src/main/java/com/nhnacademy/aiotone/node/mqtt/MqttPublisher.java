package com.nhnacademy.aiotone.node.mqtt;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.WriteApi;
import com.influxdb.client.WriteOptions;
import com.influxdb.client.domain.WritePrecision;
import com.nhnacademy.aiotone.measurement.BaseMeasurement;
import com.nhnacademy.aiotone.node.BaseNode;
import com.nhnacademy.aiotone.wire.Wire;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class MqttPublisher extends BaseNode {
    private static final int THREAD_COUNT = 3;

    private final InfluxDBClient influxDBClient;
    private final WriteApi api;
    public final List<Thread> threadList = new ArrayList<>();

    private Wire<BaseMeasurement> wire;

    public MqttPublisher(InfluxDBClient influxDBClient) {
        this.influxDBClient = influxDBClient;

        WriteOptions writeOptions = WriteOptions.builder()
                .batchSize(5_000)
                .flushInterval(1_000)
                .bufferLimit(10_000)
                .jitterInterval(1_000)
                .retryInterval(5_000)
                .build();

        api = influxDBClient.makeWriteApi(writeOptions);

        for (int i = 0; i < THREAD_COUNT; ++i) {
            Thread t = new Thread(() -> {

                while (!Thread.currentThread().isInterrupted()) {

                    if (wire != null && !wire.isEmpty()) {
                        api.writeMeasurement(WritePrecision.S, wire.poll());
                    }
                }
            });

            t.start();
            threadList.add(t);
        }
    }

    public void wireIn(Wire<BaseMeasurement> wire) {
        this.wire = wire;
    }
}
