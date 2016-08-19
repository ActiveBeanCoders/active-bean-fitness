package com.activebeancoders.fitness.util;

import java.util.Collection;
import java.util.Map;

/**
 * @author Dan Barrese
 */
public class Assert {

    public static void assertTrue(boolean condition, String msg) {
        if (!condition) {
            throw new AssertionError(msg);
        }
    }

    public static void assertTrue(boolean condition) {
        assertTrue(condition, "Boolean condition failed.");
    }

    public static void assertFalse(boolean condition, String msg) {
        if (condition) {
            throw new AssertionError(msg);
        }
    }

    public static void assertFalse(boolean condition) {
        assertFalse(condition, "Boolean condition failed.");
    }

    public static void assertNotNull(Object o, String msg) {
        if (o == null) {
            throw new AssertionError(msg);
        }
    }

    public static void assertNotNull(Object o) {
        assertNotNull(o, "Object cannot be null.");
    }

    public static void assertNotEmpty(Object o, String msg) {
        if (o == null) {
            throw new AssertionError(msg);
        }
        if (o instanceof String && ((String) o).isEmpty()) {
            throw new AssertionError(msg);
        }
        if (o instanceof Collection && ((Collection) o).isEmpty()) {
            throw new AssertionError(msg);
        }
        if (o instanceof Map && ((Map) o).isEmpty()) {
            throw new AssertionError(msg);
        }
        if (o instanceof Object[] && ((Object[]) o).length == 0) {
            throw new AssertionError(msg);
        }
    }

    public static void assertNotEmpty(Object o) {
        assertNotEmpty(o, "Object cannot be null or empty.");
    }

    public static void assertStringIsInitialized(String s, String msg) {
        if (s.matches("^\\$.*\\{.*\\}")) {
            throw new AssertionError(msg);
        }
    }

    public static void assertStringIsInitialized(String s) {
        assertStringIsInitialized(s, "String value (" + s + ") is not initialized!");
    }

}
