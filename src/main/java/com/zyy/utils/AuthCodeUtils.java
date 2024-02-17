package com.zyy.utils;

import java.util.UUID;

public class AuthCodeUtils {
    public static String getUUID(){
        UUID id=UUID.randomUUID();
        String[] uid=id.toString().split("-");
        return uid[1];
    }
}
