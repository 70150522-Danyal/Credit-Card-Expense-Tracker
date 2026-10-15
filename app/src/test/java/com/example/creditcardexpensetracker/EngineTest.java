package com.example.creditcardexpensetracker;

import org.junit.Test;
import static org.junit.Assert.*;

import com.example.creditcardexpensetracker.data.Expense;
import com.example.creditcardexpensetracker.data.Payment;
import com.example.creditcardexpensetracker.logic.Engine;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EngineTest {

    @Test
    public void testCalculateDueDate_31DaysMonth() {
        // January has 31 days. Cutoff is 21st.
        LocalDate beforeCutoff = LocalDate.of(2023, 1, 21);
        assertEquals(LocalDate.of(2023, 2, 9), Engine.calculateDueDate(beforeCutoff));

        LocalDate afterCutoff = LocalDate.of(2023, 1, 22);
        assertEquals(LocalDate.of(2023, 3, 9), Engine.calculateDueDate(afterCutoff));
    }

    @Test
    public void testCalculateDueDate_30DaysMonth() {
        // April has 30 days. Cutoff is 20th.
        LocalDate beforeCutoff = LocalDate.of(2023, 4, 20);
        assertEquals(LocalDate.of(2023, 5, 9), Engine.calculateDueDate(beforeCutoff));

        LocalDate afterCutoff = LocalDate.of(2023, 4, 21);
        assertEquals(LocalDate.of(2023, 6, 9), Engine.calculateDueDate(afterCutoff));
    }

    @Test
    public void testCalculateFinalAmountPkr_Local() {
        BigDecimal amount = new BigDecimal("1000.00");
        BigDecimal fx = new BigDecimal("1.00");
        BigDecimal expected = new BigDecimal("1000.00");
        assertEquals(expected, Engine.calculateFinalAmountPkr(amount, fx, false, false));
    }

    @Test
    public void testCalculateFinalAmountPkr_International_NotIt() {
        // Original: 10, FX: 280 -> Base: 2800.00
        // FTF (4%): 112.00
        // FED (16% of FTF): 17.92
        // Adv Tax (0.5% of base): 14.00
        // Total: 2800 + 112 + 17.92 + 14 = 2943.92
        BigDecimal amount = new BigDecimal("10.00");
        BigDecimal fx = new BigDecimal("280.00");
        BigDecimal expected = new BigDecimal("2943.92");
        assertEquals(expected, Engine.calculateFinalAmountPkr(amount, fx, true, false));
    }

    @Test
    public void testCalculateFinalAmountPkr_International_ItService() {
        // Same base: 2800.00
        // PRA (5% of base): 140.00
        // Total: 2943.92 + 140 = 3083.92
        BigDecimal amount = new BigDecimal("10.00");
        BigDecimal fx = new BigDecimal("280.00");
        BigDecimal expected = new BigDecimal("3083.92");
        assertEquals(expected, Engine.calculateFinalAmountPkr(amount, fx, true, true));
    }

    @Test
    public void testMatchPayments() {
        List<Expense> expenses = new ArrayList<>();
        
        Expense e1 = new Expense();
        e1.id = 1;
        e1.dueDate = LocalDate.of(2023, 2, 9);
        e1.finalAmountPkr = new BigDecimal("1000.00");
        expenses.add(e1);
        
        Expense e2 = new Expense();
        e2.id = 2;
        e2.dueDate = LocalDate.of(2023, 3, 9);
        e2.finalAmountPkr = new BigDecimal("2000.00");
        expenses.add(e2);

        List<Payment> payments = new ArrayList<>();
        
        // Linked payment to e2
        Payment p1 = new Payment();
        p1.id = 1;
        p1.expenseId = 2;
        p1.amount = new BigDecimal("500.00");
        payments.add(p1);
        
        // Lump sum payment
        Payment p2 = new Payment();
        p2.id = 2;
        p2.expenseId = null;
        p2.amount = new BigDecimal("1200.00");
        payments.add(p2);

        Engine.PaymentSummary summary = Engine.matchPayments(expenses, payments);

        // e1 should be fully paid by lump sum (1000)
        assertEquals(new BigDecimal("1000.00"), summary.expensePaidMap.get(1));
        
        // e2 should have 500 (linked) + 200 (remaining lump sum) = 700
        assertEquals(new BigDecimal("700.00"), summary.expensePaidMap.get(2));
        
        // credit should be 0
        assertEquals(new BigDecimal("0.00"), summary.credit);
    }
    
    @Test
    public void testMatchPayments_Overpayment() {
        List<Expense> expenses = new ArrayList<>();
        Expense e1 = new Expense();
        e1.id = 1;
        e1.dueDate = LocalDate.of(2023, 2, 9);
        e1.finalAmountPkr = new BigDecimal("1000.00");
        expenses.add(e1);
        
        List<Payment> payments = new ArrayList<>();
        Payment p1 = new Payment();
        p1.id = 1;
        p1.expenseId = null;
        p1.amount = new BigDecimal("1500.00");
        payments.add(p1);
        
        Engine.PaymentSummary summary = Engine.matchPayments(expenses, payments);
        assertEquals(new BigDecimal("1000.00"), summary.expensePaidMap.get(1));
        assertEquals(new BigDecimal("500.00"), summary.credit);
    }
}
