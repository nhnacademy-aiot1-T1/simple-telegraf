package com.nhnacademy.aiotone.service;

import com.nhnacademy.aiotone.entity.MotorStatus;
import com.nhnacademy.aiotone.repository.MotorStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MotorStatusService {
    private final MotorStatusRepository motorStatusRepository;

    @Transactional
     public void save(MotorStatus motorStatus) {
        motorStatusRepository.save(motorStatus);
    }
}
