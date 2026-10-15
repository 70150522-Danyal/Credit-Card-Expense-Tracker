package com.example.creditcardexpensetracker.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;
import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM users ORDER BY userNumber ASC")
    LiveData<List<User>> getAllUsers();
    
    @Query("SELECT * FROM users ORDER BY userNumber ASC")
    List<User> getAllUsersSync();

    @Insert
    void insertUser(User user);
    
    @Update
    void updateUser(User user);
    
    @Delete
    void deleteUser(User user);
}
