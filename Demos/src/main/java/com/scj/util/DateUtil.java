package com.scj.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2017/3/12 0012.
 */
public class DateUtil {

    public static String getSystemTimeYYYYMM(){
        Calendar calendar =Calendar.getInstance();
        Date date =calendar.getTime();
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    public static void main(String[] args) {
        System.out.println(getSystemTimeYYYYMM());
    }
}
