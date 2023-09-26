package com.jm.model;

import com.jm.mock.MockModel;
import java.util.Date;

public class Transaction extends MockModel{
    private String tranId;
    private String accountNumber;
    private Date transactionTs;
    private String type;
    private double amount;


    public void setTranId(String tranId) {
        this.tranId = tranId;
    }

    public String getTranId() {
        return tranId;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setTransactionTs(Date transactionTs) {
        this.transactionTs = transactionTs;
    }

    public Date getTransactionTs() {
        return transactionTs;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }
}
