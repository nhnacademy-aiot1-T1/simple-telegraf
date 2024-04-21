package com.nhnacademy.aiotone.wire;

import lombok.Getter;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Getter
public class Wire <T> {
    private final BlockingQueue<T> messageQue = new LinkedBlockingQueue<>();

    public boolean isEmpty() {
        return messageQue.isEmpty();
    }

    public T poll() {
        return messageQue.poll();
    }

    public void add(T element) {
        messageQue.add(element);
    }
}
