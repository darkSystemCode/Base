package com.sy.util;

import java.lang.reflect.ParameterizedType;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

/**
 * @Author: shuYan
 * @Date: 2023/5/2 23:52
 * @Descript:
 */
public class Utils {

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String getTableName(Class clazz) {
        return clazz.getSimpleName();
    }

    public static String getTableNameToLower(Class clazz) {
        return clazz.getSimpleName().toLowerCase();
    }

    public static String getTableNameToUpper(Class clazz) {
        return clazz.getSimpleName().toUpperCase();
    }

    public static String getCurrentDateTime() {
        Calendar calendar = Calendar.getInstance();
        return FORMAT.format(calendar.getTime()).toString();
    }
}
