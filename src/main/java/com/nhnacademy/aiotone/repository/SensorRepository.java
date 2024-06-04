package com.nhnacademy.aiotone.repository;

import com.nhnacademy.aiotone.entity.Sensor;
import org.springframework.data.repository.CrudRepository;

public interface SensorRepository extends CrudRepository<Sensor, Long> {
}
