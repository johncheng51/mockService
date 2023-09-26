package com.jm.model;

public class Result {
    private Account account;
    private Transaction transaction;
    public Result(Account account,Transaction transaction){
        this.account=account;
        this.transaction=transaction;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Account getAccount() {
        return account;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public Transaction getTransaction() {
        return transaction;
    }

}
