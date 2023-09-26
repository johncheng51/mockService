package com.jm.service;

import com.jm.mock.MockModel;
import com.jm.mock.MockUtil;
import com.jm.model.Account;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AccountService extends AbsService{


    @Override
    protected void setPrimaryKey() {this.primaryKey="accountNumber";}
     

    @Override
    protected List<MockModel> loadObjects() {
        List<Account>  list=( List<Account>) (Object) MockUtil.create(100, Account.class,getPrimaryKey());
        list.forEach(x->x.setBalance(round(x.getBalance())));
        return ( List<MockModel>) (Object) list;
    }

    @Override
    protected String getFileName() {  return "Account.txt"; }
}
