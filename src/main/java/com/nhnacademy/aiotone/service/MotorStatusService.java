package com.nhnacademy.aiotone.service;

import com.nhnacademy.aiotone.entity.Motor;
import com.nhnacademy.aiotone.entity.MotorStatus;
import com.nhnacademy.aiotone.entity.Sensor;
import com.nhnacademy.aiotone.properties.MotorChannelInfoProperties;
import com.nhnacademy.aiotone.repository.MotorRepository;
import com.nhnacademy.aiotone.repository.MotorStatusRepository;
import com.nhnacademy.aiotone.repository.SensorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class MotorStatusService {
    private final Map<Integer, String> sensorMap = new HashMap<>();
    private final MotorRepository motorRepository;
    private final MotorStatusRepository motorStatusRepository;
    private final SensorRepository sensorRepository;

    public MotorStatusService(MotorRepository motorRepository,
                              SensorRepository sensorRepository,
                              MotorStatusRepository motorStatusRepository,
                              MotorChannelInfoProperties motorChannelInfoProperties
                              ) {
        this.motorRepository = motorRepository;
        this.sensorRepository = sensorRepository;
        this.motorStatusRepository = motorStatusRepository;

        int count = MotorChannelInfoProperties.class.getDeclaredFields().length;
        for (int i = 0; i < count; ++i) {

            String channelSensor = motorChannelInfoProperties.getChannelSensor(i);
            if (StringUtils.hasText(channelSensor)) {
                sensorMap.put(i, channelSensor);
            }

        }
    }

    @Transactional
     public void save(MotorStatus motorStatus) {
        Motor motor;
        if (!motorRepository.existsByMotorName(motorStatus.getMotorName())) {
            motor = new Motor();

            motor.setDeviceName(motorStatus.getMotorName());
            motor.setMotorName(motorStatus.getMotorName());

            motorRepository.save(motor);
            for (int channelNo : sensorMap.keySet()) {
                Sensor sensor = Sensor.builder()
                        .motorId(motor.getId())
                        .channelNo(channelNo)
                        .sensorType(sensorMap.get(channelNo))
                        .build();

                sensorRepository.save(sensor);
            }
        }
        else {
            motor = motorRepository.getMotorByDeviceName(motorStatus.getMotorName());
        }
        motor.setIsOn(motorStatus.getIsOn());

        motorStatus.setMotorId(motor.getId());
        motorStatusRepository.save(motorStatus);
    }
}
