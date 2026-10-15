package com.example.creditcardexpensetracker.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;
import java.util.List;

@Dao
public interface PaymentDao {
    @Query("SELECT * FROM payments ORDER BY date ASC")
    LiveData<List<Payment>> getAllPayments();
    
    @Query("SELECT * FROM payments WHERE userNumber = :userNumber ORDER BY date ASC")
    LiveData<List<Payment>> getPaymentsByUser(int userNumber);

    @Query("SELECT * FROM payments ORDER BY date ASC")
    List<Payment> getAllPaymentsSync();
    
    @Query("SELECT * FROM payments WHERE userNumber = :userNumber ORDER BY date ASC")
    List<Payment> getPaymentsByUserSync(int userNumber);

    @Insert
    void insertPayment(Payment payment);
    
    @Update
    void updatePayment(Payment payment);
    
    @Delete
    void deletePayment(Payment payment);
}
