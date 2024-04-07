package com.zyy.utils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class DateUtils {
    public static Date getNow(){
        LocalDate localDate=LocalDate.now();
        ZoneId zoneId=ZoneId.of("Asia/Shanghai");
        Date nowDate=Date.from(localDate.atStartOfDay(zoneId).toInstant());
        return nowDate;
    }
}
