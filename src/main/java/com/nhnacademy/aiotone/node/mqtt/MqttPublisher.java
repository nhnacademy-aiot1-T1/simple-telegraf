package com.nhnacademy.aiotone.node.mqtt;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.WriteApiBlocking;
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
    private final WriteApiBlocking api;
    public final List<Thread> threadList = new ArrayList<>();

    private Wire<BaseMeasurement> wire;

    public MqttPublisher(InfluxDBClient influxDBClient) {
        this.influxDBClient = influxDBClient;
        api = influxDBClient.getWriteApiBlocking();

        for (int i = 0; i < THREAD_COUNT; ++i) {
            Thread t = new Thread(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    if (wire != null && !wire.isEmpty()) {
                        api.writeMeasurement(WritePrecision.MS, wire.poll());
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
