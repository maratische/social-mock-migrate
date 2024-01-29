package com.examples.uidcreator.controller;

public class StringUtils {

    public static boolean hasValue(String str) {
        return str != null && str.length() > 0;
    }

    public static boolean isEmpty(String str) {
        return !hasValue(str);
    }
}
