package com.ldzhn.baseframework.utils;

import java.util.UUID;

/**
 * Created by MaJingcao on 2018/3/14.
 * Copyright by syswin
 */
public class UUIDUtils {

    public static String generateValue() {
        return generateValue(UUID.randomUUID().toString());
    }

    public static String generateValue(String param) {
        return UUID.fromString(UUID.nameUUIDFromBytes(param.getBytes()).toString()).toString();
    }

}
