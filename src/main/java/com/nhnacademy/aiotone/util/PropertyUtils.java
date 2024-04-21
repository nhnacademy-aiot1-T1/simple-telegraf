package com.nhnacademy.aiotone.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class PropertyUtils {

    private PropertyUtils() {
        throw new IllegalStateException("util 클래스 입니다.");
    }

    public static void insertProperties(Properties properties, String prefix, Object object) {
        Class<?> clazz = object.getClass();

        for (Field field : clazz.getDeclaredFields()) {
            if (Modifier.isStatic(field.getModifiers())) continue; // static field면 스킵합니다.

            try {
                clazz.getMethod(getGetterMethodName(field.getName())); // getter method가 존재하면 값을 넣어줍니다.

                field.setAccessible(true);
                field.set(object, properties.get(prefix + "." + field.getName()));

            } catch (Exception ignore) {
            }
        }
    }

    /**
     * fieldName을 기준으로 getter 함수 이름을 반환합니다.
     * ex) fieldName이 url일 경우 getUrl을 리턴
     */
    private static String getGetterMethodName(String fieldName) {
        byte[] f = fieldName.getBytes(StandardCharsets.UTF_8);
        f[0] -= 32; // to uppercase.

        return "get" + new String(f);
    }
}
