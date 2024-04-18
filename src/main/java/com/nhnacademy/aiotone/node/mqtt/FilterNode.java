package com.nhnacademy.aiotone.node.mqtt;

import com.nhnacademy.aiotone.measurement.BaseMeasurement;
import com.nhnacademy.aiotone.node.RunnableNode;
import com.nhnacademy.aiotone.wire.Wire;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.List;

@Slf4j
public class FilterNode extends RunnableNode {
    private final List<Wire<BaseMeasurement>> list = new LinkedList<>();
    private Wire<BaseMeasurement> wire;
    private long filteredCount;

    public void wireIn(Wire<BaseMeasurement> wire) {
        this.wire = wire;
    }

    public void wireOut(Wire<BaseMeasurement> wire) {
        list.add(wire);
    }

    @Override
    public void process() {
        if (!wire.isEmpty()) {
            BaseMeasurement measurement = wire.poll();

            if (measurement == null || measurement.getValue() == null) {
                filteredCount++;

                log.info("filtered count : {}", filteredCount);
            }
            else {
                for (Wire<BaseMeasurement> out : list) {
                    out.add(measurement);
                }
            }
        }
    }
}
