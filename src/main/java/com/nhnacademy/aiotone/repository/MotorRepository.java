package com.nhnacademy.aiotone.repository;

import com.nhnacademy.aiotone.entity.Motor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;

import javax.persistence.QueryHint;

public interface MotorRepository extends CrudRepository<Motor, Long> {
    @QueryHints(value = { @QueryHint(name = "org.hibernate.cacheable", value = "true") })
    @Query("SELECT COUNT(m) > 0 FROM Motor m WHERE m.motorName = :motorName")
    boolean existsByMotorName(String motorName);

    Motor getMotorByDeviceName(String deviceName);
}
