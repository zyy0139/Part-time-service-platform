package com.zyy.util;

import java.util.UUID;

public class AuthCodeUtil {
    public static String getUUID(){
        UUID id=UUID.randomUUID();
        String[] uid=id.toString().split("-");
        return uid[1];
    }
}
