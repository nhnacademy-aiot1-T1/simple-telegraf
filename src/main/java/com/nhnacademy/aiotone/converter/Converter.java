package com.nhnacademy.aiotone.converter;

public interface Converter<F, T> {
    T convert(F from) throws InterruptedException;
}
