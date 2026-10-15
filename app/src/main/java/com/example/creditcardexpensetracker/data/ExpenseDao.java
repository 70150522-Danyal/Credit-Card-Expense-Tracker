package com.example.creditcardexpensetracker.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;
import java.util.List;

@Dao
public interface ExpenseDao {
    @Query("SELECT * FROM expenses ORDER BY date ASC")
    LiveData<List<Expense>> getAllExpenses();
    
    @Query("SELECT * FROM expenses WHERE userNumber = :userNumber ORDER BY date ASC")
    LiveData<List<Expense>> getExpensesByUser(int userNumber);

    @Query("SELECT * FROM expenses ORDER BY date ASC")
    List<Expense> getAllExpensesSync();
    
    @Query("SELECT * FROM expenses WHERE userNumber = :userNumber ORDER BY date ASC")
    List<Expense> getExpensesByUserSync(int userNumber);

    @Insert
    void insertExpense(Expense expense);
    
    @Update
    void updateExpense(Expense expense);
    
    @Delete
    void deleteExpense(Expense expense);
}
