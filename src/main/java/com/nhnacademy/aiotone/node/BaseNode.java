package com.nhnacademy.aiotone.node;


import lombok.Getter;

import java.util.UUID;

@Getter
public abstract class BaseNode implements Runnable {
    private UUID id;
}
