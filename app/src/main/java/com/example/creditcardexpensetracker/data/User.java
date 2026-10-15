package com.example.creditcardexpensetracker.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
    @PrimaryKey
    public int userNumber;
    
    public String displayName;
    
    public boolean isAdmin;
    
    public User(int userNumber, String displayName, boolean isAdmin) {
        this.userNumber = userNumber;
        this.displayName = displayName;
        this.isAdmin = isAdmin;
    }
}
