package com.zyy.utils;

import java.util.Random;

public class RadomUtils {
    public static String creatId(){
        Random random=new Random();
        String id=String.valueOf(random.nextInt(99999)+300000);
        return id;
    }
}
