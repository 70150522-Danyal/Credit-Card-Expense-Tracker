package com.example.creditcardexpensetracker.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity(tableName = "expenses")
public class Expense {
    @PrimaryKey(autoGenerate = true)
    public int id;
    
    public int userNumber;
    
    public LocalDate date;
    
    public String description;
    
    public BigDecimal originalAmount;
    
    public String currency;
    
    public BigDecimal fxRate;
    
    public boolean isItService;
    
    public BigDecimal finalAmountPkr;
    
    public LocalDate dueDate;
    
    public Expense() {}
}
