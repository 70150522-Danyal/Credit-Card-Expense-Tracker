package com.example.creditcardexpensetracker.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity(tableName = "payments")
public class Payment {
    @PrimaryKey(autoGenerate = true)
    public int id;
    
    public int userNumber;
    
    public LocalDate date;
    
    public BigDecimal amount;
    
    public Integer expenseId; // null if lump sum
    
    public String imagePath; // null if no screenshot
    
    public Payment() {}
}
