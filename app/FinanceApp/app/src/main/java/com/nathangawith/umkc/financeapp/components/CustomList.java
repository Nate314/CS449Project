package com.nathangawith.umkc.financeapp.components;

public class CustomList {

    private int mImgResID;
    private String mTransactionDescription;
    private String mTransactionAmount;

    public CustomList(int imgID, String transactionDescription, String transactionAmount) {
        this.mImgResID = imgID;
        this.mTransactionDescription = transactionDescription;
        this.mTransactionAmount = transactionAmount;
    }

    public int getImgResID() { return this.mImgResID; }
    public String getTransactionDescription() { return this.mTransactionDescription; }
    public String getTransactionAmount() { return this.mTransactionAmount; }
}
