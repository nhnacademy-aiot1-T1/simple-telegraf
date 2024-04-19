package com.nhnacademy.aiotone.node.mqtt;

import com.nhnacademy.aiotone.measurement.BaseMeasurement;
import com.nhnacademy.aiotone.node.RunnableNode;
import com.nhnacademy.aiotone.node.filter.MeasurementFilter;
import com.nhnacademy.aiotone.wire.Wire;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Slf4j
public class FilterNode extends RunnableNode {
    private final List<MeasurementFilter> filters = new ArrayList<>();
    private final List<Wire<BaseMeasurement>> list = new LinkedList<>();
    private Wire<BaseMeasurement> wire;
    private long filteredCount;


    public void wireIn(Wire<BaseMeasurement> wire) {
        this.wire = wire;
    }

    public void wireOut(Wire<BaseMeasurement> wire) {
        list.add(wire);
    }

    public void addFilter(MeasurementFilter filter) {
        filters.add(filter);
    }

    /**
     * filters에 등록된 순서대로 measurement 데이터들을 필터링 합니다.
     */
    @Override
    public void process() {
        if (!wire.isEmpty()) {
            BaseMeasurement measurement = wire.poll();

            for (MeasurementFilter filter : filters) {
                if (!filter.isPassed(measurement)) {
                    log.info("filtered count : {}", ++filteredCount);

                    return;
                }
            }

            for (Wire<BaseMeasurement> out : list) {
                out.add(measurement);
            }
        }
    }
}
