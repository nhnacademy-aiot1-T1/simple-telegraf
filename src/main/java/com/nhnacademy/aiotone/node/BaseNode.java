package com.nhnacademy.aiotone.node;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public abstract class BaseNode {
    protected final UUID id = UUID.randomUUID();
    protected final LocalDateTime createdDateTimeAt = LocalDateTime.now();
}
