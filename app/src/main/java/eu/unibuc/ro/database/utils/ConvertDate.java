package eu.unibuc.ro.database.utils;

import androidx.room.TypeConverter;

import java.util.Date;

public class ConvertDate {

    @TypeConverter
    public static Long dateToTimestamp (Date date){
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public  static Date timestampToDate(Long value){
        return value == null ? null : new Date(value);
    }
}
