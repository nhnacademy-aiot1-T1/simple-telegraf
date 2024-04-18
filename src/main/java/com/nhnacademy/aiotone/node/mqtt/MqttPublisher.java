package com.nhnacademy.aiotone.node.mqtt;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApi;
import com.influxdb.client.domain.WritePrecision;
import com.nhnacademy.aiotone.measurement.BaseMeasurement;
import com.nhnacademy.aiotone.node.RunnableNode;
import com.nhnacademy.aiotone.properties.MqttPublisherProperties;
import com.nhnacademy.aiotone.wire.Wire;
import lombok.Getter;

@Getter
public class MqttPublisher extends RunnableNode {
    private final InfluxDBClient influxDBClient;
    private final WriteApi api;

    private Wire<BaseMeasurement> wire;

    public MqttPublisher(MqttPublisherProperties properties) {
        influxDBClient = InfluxDBClientFactory.create(properties.getUrl(),
                properties.getToken().toCharArray(),
                properties.getOrg(),
                properties.getBucket());

        api = influxDBClient.makeWriteApi();
    }

    public void wireIn(Wire<BaseMeasurement> wire) {
        this.wire = wire;
    }

    @Override
    public void process() {
        if (!wire.isEmpty()) {
            api.writeMeasurement(WritePrecision.S, wire.poll());
        }

        api.flush();
    }
}
