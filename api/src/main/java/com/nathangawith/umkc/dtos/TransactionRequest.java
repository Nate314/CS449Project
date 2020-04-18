package com.nathangawith.umkc.dtos;

import java.util.Date;

public class TransactionRequest {
    public int TransactionID;
    public int UserID;
    public int AccountID;
    public int CategoryID;
    public int AccountFromID;
    public int AccountToID;
    public int CategoryFromID;
    public int CategoryToID;
    public String Description;
    public double Amount;
    public Date Date;
}
