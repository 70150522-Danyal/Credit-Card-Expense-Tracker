package com.example.creditcardexpensetracker.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface SettingDao {
    @Query("SELECT value FROM settings WHERE `key` = :key LIMIT 1")
    LiveData<String> getSetting(String key);

    @Query("SELECT value FROM settings WHERE `key` = :key LIMIT 1")
    String getSettingSync(String key);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSetting(Setting setting);
}
