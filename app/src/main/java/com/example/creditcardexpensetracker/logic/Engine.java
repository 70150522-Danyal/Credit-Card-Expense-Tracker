package com.example.creditcardexpensetracker.logic;

import com.example.creditcardexpensetracker.data.Expense;
import com.example.creditcardexpensetracker.data.Payment;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Engine {

    /**
     * Calculates the due date for a given expense date.
     * The cutoff is the 21st in 31-day months and the 20th in all other months.
     * Expenses on or before the cutoff are due on the 9th of the next month; 
     * later expenses are due on the 9th of the month after.
     */
    public static LocalDate calculateDueDate(LocalDate expenseDate) {
        YearMonth yearMonth = YearMonth.from(expenseDate);
        int daysInMonth = yearMonth.lengthOfMonth();
        int cutoffDay = (daysInMonth == 31) ? 21 : 20;

        if (expenseDate.getDayOfMonth() <= cutoffDay) {
            // Due on the 9th of the next month
            return expenseDate.plusMonths(1).withDayOfMonth(9);
        } else {
            // Due on the 9th of the month after next
            return expenseDate.plusMonths(2).withDayOfMonth(9);
        }
    }

    /**
     * Calculates international fees and returns the final PKR amount.
     * FTF (4%), FED (16% of FTF), Advance Tax (0.5%), and for IT services PRA sales tax (5%).
     * Money: BigDecimal, rounded to 2 decimals (half up).
     */
    public static BigDecimal calculateFinalAmountPkr(BigDecimal originalAmount, BigDecimal fxRate, boolean isInternational, boolean isItService) {
        if (!isInternational) {
            // Local expenses are already in PKR, fxRate should be 1, but we just multiply
            return originalAmount.multiply(fxRate).setScale(2, RoundingMode.HALF_UP);
        }

        BigDecimal basePkr = originalAmount.multiply(fxRate).setScale(2, RoundingMode.HALF_UP);

        BigDecimal ftf = basePkr.multiply(new BigDecimal("0.04")).setScale(2, RoundingMode.HALF_UP);
        BigDecimal fed = ftf.multiply(new BigDecimal("0.16")).setScale(2, RoundingMode.HALF_UP);
        BigDecimal advTax = basePkr.multiply(new BigDecimal("0.005")).setScale(2, RoundingMode.HALF_UP);
        
        BigDecimal praTax = BigDecimal.ZERO;
        if (isItService) {
            praTax = basePkr.multiply(new BigDecimal("0.05")).setScale(2, RoundingMode.HALF_UP);
        }

        return basePkr.add(ftf).add(fed).add(advTax).add(praTax).setScale(2, RoundingMode.HALF_UP);
    }

    public static class PaymentSummary {
        public Map<Integer, BigDecimal> expensePaidMap = new HashMap<>(); // expenseId -> amount paid
        public BigDecimal credit = BigDecimal.ZERO; // overpayment
    }

    /**
     * Matches payments to expenses. 
     * A payment can be linked to one expense ("Mark paid") or made as a lump sum,
     * which is applied to the oldest due expenses first.
     */
    public static PaymentSummary matchPayments(List<Expense> expenses, List<Payment> payments) {
        PaymentSummary summary = new PaymentSummary();
        
        // Initialize map
        for (Expense e : expenses) {
            summary.expensePaidMap.put(e.id, BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
        }
        
        // Sort expenses by due date (oldest first)
        expenses.sort((o1, o2) -> o1.dueDate.compareTo(o2.dueDate));

        BigDecimal lumpSumAvailable = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);

        for (Payment p : payments) {
            if (p.expenseId != null) {
                // Linked payment
                BigDecimal currentPaid = summary.expensePaidMap.get(p.expenseId);
                if (currentPaid == null) currentPaid = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
                summary.expensePaidMap.put(p.expenseId, currentPaid.add(p.amount != null ? p.amount : BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)));
            } else {
                // Lump sum
                lumpSumAvailable = lumpSumAvailable.add(p.amount != null ? p.amount : BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
            }
        }

        // Apply lump sums to oldest due expenses first
        for (Expense e : expenses) {
            if (lumpSumAvailable.compareTo(BigDecimal.ZERO) <= 0) break;

            BigDecimal amountPaid = summary.expensePaidMap.get(e.id);
            BigDecimal unpaid = e.finalAmountPkr.subtract(amountPaid);

            if (unpaid.compareTo(BigDecimal.ZERO) > 0) {
                if (lumpSumAvailable.compareTo(unpaid) >= 0) {
                    summary.expensePaidMap.put(e.id, amountPaid.add(unpaid));
                    lumpSumAvailable = lumpSumAvailable.subtract(unpaid);
                } else {
                    summary.expensePaidMap.put(e.id, amountPaid.add(lumpSumAvailable));
                    lumpSumAvailable = BigDecimal.ZERO;
                }
            }
        }

        summary.credit = lumpSumAvailable.setScale(2, RoundingMode.HALF_UP);
        return summary;
    }
}
