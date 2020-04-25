package com.nathangawith.umkc.financeapp.components;

import com.nathangawith.umkc.financeapp.R;
import com.nathangawith.umkc.financeapp.dtos.TransactionDto;

public class RegisterEntry {

    private int imgResID;
    private String description;
    private String amount;
    private String date;
    private String label1;
    private String label2;
    private String value1;
    private String value2;

    private String stringifyTransactionAmount(double amount) {
        boolean negative = amount < 0;
        amount = Math.abs(amount);
        int intCents = ((int) ((amount * 100) % 100));
        String dollars = ((int) amount) + "";
        String cents = String.format("%s%s", intCents < 10 ? "0" : "", intCents + "");
        return String.format("%s$%s.%s", negative ? "-" : "", dollars, cents);
    }

    public RegisterEntry(TransactionDto transaction) {
        this.imgResID = transaction.IsTransfer ? R.drawable.transfer_blue
                : (transaction.IsIncome ? R.drawable.income_green : R.drawable.expense_red);
        this.description = transaction.Description;
        this.amount = this.stringifyTransactionAmount(transaction.Amount);
        this.date = transaction.Date;

        if (transaction.IsTransfer) {
            if (transaction.AccountFromDescription != null) {
                this.label1 = "From Account:";
                this.label2 = "To Account:";
                this.value1 = transaction.AccountFromDescription;
                this.value2 = transaction.AccountToDescription;
            } else {
                this.label1 = "From Category:";
                this.label2 = "To Category:";
                this.value1 = transaction.CategoryFromDescription;
                this.value2 = transaction.CategoryToDescription;
            }
        } else {
            this.label1 = "Account:";
            this.label2 = "Category:";
            this.value1 = transaction.AccountDescription;
            this.value2 = transaction.CategoryDescription;
        }
    }

    public int getImgResID() { return this.imgResID; }
    public String getDescription() { return this.description; }
    public String getAmount() { return this.amount; }
    public String getDate() { return this.date; }
    public String getLabel1() { return this.label1; }
    public String getLabel2() { return this.label2; }
    public String getValue1() { return this.value1; }
    public String getValue2() { return this.value2; }
}
