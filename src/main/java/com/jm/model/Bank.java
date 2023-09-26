package com.jm.model;

import com.jm.mock.MockModel;

public class Bank extends MockModel{
   private String name;
   private String cardNo;
   
   
   
   @Override
   public String incKeys(){
       return "name";
   }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
