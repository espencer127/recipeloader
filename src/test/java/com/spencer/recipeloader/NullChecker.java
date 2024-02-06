package com.spencer.recipeloader;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;

public class NullChecker {
    // https://www.baeldung.com/java-check-all-variables-object-null

    public static boolean anyNull(Object target) {

        if (target == null) {
            return true;
        }

        return Arrays.stream(target.getClass()
                .getDeclaredFields())
                .peek(f -> f.setAccessible(true))
                .map(f -> getFieldValue(f, target))
                .anyMatch(Objects::isNull);
    }

    private static Object getFieldValue(Field field, Object target) {
        try {
            return field.get(target);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}