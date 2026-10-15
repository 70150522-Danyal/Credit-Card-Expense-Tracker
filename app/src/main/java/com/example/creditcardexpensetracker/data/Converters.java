package com.example.creditcardexpensetracker.data;

import androidx.room.TypeConverter;
import java.math.BigDecimal;
import java.time.LocalDate;

public class Converters {
    @TypeConverter
    public String fromBigDecimal(BigDecimal value) {
        return value == null ? null : value.toString();
    }

    @TypeConverter
    public BigDecimal toBigDecimal(String value) {
        return value == null ? null : new BigDecimal(value);
    }
    
    @TypeConverter
    public String fromLocalDate(LocalDate date) {
        return date == null ? null : date.toString();
    }
    
    @TypeConverter
    public LocalDate toLocalDate(String value) {
        return value == null ? null : LocalDate.parse(value);
    }
}
