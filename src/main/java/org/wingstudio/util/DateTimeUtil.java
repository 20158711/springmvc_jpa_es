package org.wingstudio.util;

import org.apache.commons.lang3.StringUtils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtil {
    public static final String STANDAND_FORMAT="yyyy-MM-dd HH:mm:ss";

    public static Date strToDate(String dateStr,String format) throws ParseException {
        SimpleDateFormat sdf=new SimpleDateFormat(format);
        Date date=sdf.parse(dateStr);
        return date;
    }

    public static Date strToDate(String dateStr) throws ParseException {
        return strToDate(dateStr,STANDAND_FORMAT);
    }

    public static String dateToStr(Date date,String format){
        if(date==null){
            return StringUtils.EMPTY;
        }
        SimpleDateFormat sdf=new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static String dateToStr(Date date){
        return dateToStr(date,STANDAND_FORMAT);
    }

    public static void main(String[] args) {
        System.out.println(dateToStr(new Date()));
    }

}
