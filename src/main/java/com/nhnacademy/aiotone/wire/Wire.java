package com.nhnacademy.aiotone.wire;

import lombok.Getter;

import java.util.LinkedList;
import java.util.Queue;

@Getter
public class Wire <T> {
    private final Queue<T> messageQue = new LinkedList<>();

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
