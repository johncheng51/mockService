package com.jm.service;

import com.jm.mock.MockModel;
import com.jm.mock.MockUtil;
import com.jm.model.Account;
import com.jm.model.Result;
import com.jm.model.Search;
import com.jm.model.Transaction;
import com.jm.util.Util;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService extends AbsService{
    public static final String DEPOSIT="DEPOSIT";
    public static final String WITHDRAW="WITHDRAW";
    public static final String ACCOUNTNUMBER="accountNumber";
    public static final String ACCTERROR="Account %s is not exist for withdraw";
    public static final String NOTENOUGH="The balance[%s] is not enough for withdraw [%s]";
    public static final String NAGATIVE="amount[%s] can not be nagative";
    @Autowired
    protected AccountService accountService;
       
    @Override
    protected void setPrimaryKey() {this.primaryKey="tranId";}
   
    @Override
    protected List<MockModel> loadObjects() {
        List<Transaction>  list=( List<Transaction>) (Object) MockUtil.create(100, Transaction.class,getPrimaryKey());
        list.forEach(x->x.setAmount(round(x.getAmount())));
        list.forEach(x->x.setType(isOdd(x.getAmount())?DEPOSIT:WITHDRAW));
        return ( List<MockModel>) (Object) list;
     
    }
    
    

    @Override
    protected String getFileName() { return "Transaction.txt";  }
    
    private Transaction create(String accountNumber, double amount,boolean isDeposit){
        Transaction transaction=new Transaction();
        transaction.setAccountNumber(accountNumber);
        transaction.setAmount(amount);
        transaction.setType(isDeposit? DEPOSIT:WITHDRAW);
        transaction.setTransactionTs(new Date());
        transaction.setTranId("tran"+this.getMax());
        return transaction;
    }
    
    private Account getAccount(String accountNumber){
        Search search=new Search(ACCOUNTNUMBER,accountNumber);
        List<Account> list=(List<Account>)(Object) accountService.search(search);
        if (list.size()==0) return null;
        else return list.get(0);
    }
    
    private void createAccount(String accountNumber,double amount){
        Account account=new Account();
        account.setAccountNumber(accountNumber);
        account.setBalance(amount);
        account.setLastUpdateTimestamp(new Date());
        accountService.create(account);
          }

    public Result withdraw(String accountNumber, double amount) {
        if (amount<0) throw error(NAGATIVE,amount+"");
        Transaction transaction=create(accountNumber,amount,false);
        Account account=getAccount(accountNumber);
        if (account==null) throw new RuntimeException(String.format(ACCTERROR,accountNumber));
        double balance=account.getBalance()-amount;
        if (balance<0) throw new RuntimeException(String.format(NOTENOUGH,account.getBalance()+"",amount));
        account.setBalance(balance);
        account.setLastUpdateTimestamp(transaction.getTransactionTs());
        accountService.update(account);
        create(transaction);
        accountService.update(account);
        return new Result(account,transaction);
    }
    
    
    public Result deposit(String accountNumber, double amount) {
        if (amount<0) throw error(NAGATIVE,amount+"");
        Transaction transaction=create(accountNumber,amount,true);
        Account account=getAccount(accountNumber);
        if (account==null) createAccount(accountNumber,amount);
        else {
            double balance=account.getBalance()+amount;
            account.setBalance(balance);
            account.setLastUpdateTimestamp(transaction.getTransactionTs());
            accountService.update(account);
        }
        create(transaction);
       return new Result(account,transaction); 
    }
    
    private int[] lastMonth(){
        Date date=new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("YYYY MM");
        String dateStr=sdf.format(date);
        String[] sa=Util.split(dateStr," ");
        int month=Integer.parseInt(sa[1]);
        int year= Integer.parseInt(sa[0]);
        if (month==1){ month=12;year--;}
        else month--;
        int endDate=0;
        switch(month){
        case 1:case 3: case 5: case 7:case 8: case 10: case 12:endDate=31;break;
        case 4:case 6: case 9:case 11:endDate=30;break;
        case 2: endDate=(year%4==0)? 29:28;
       }
        return new int[] {year,month,endDate};   
    }
    
    private String intToString(int n){
        String result=n+"";
        return (result.length()==1)? "0"+result:result;
    }
    
    
    private List<Transaction> filter(List<Transaction> list,boolean isDeposit){
       return  list.stream().filter(x->x.getType().equals(isDeposit? DEPOSIT:WITHDRAW)).collect(Collectors.toList());
    }
    
    public List<Transaction> searchLastMonth(String accountNumber,boolean isDeposit){
        List<Transaction> list=searchLastMonth(accountNumber);
        return filter(list,isDeposit);
    }
    
    public List<Transaction> searchLast7Day(String accountNumber,boolean isDeposit){
        List<Transaction> list=searchLast7Day(accountNumber);
        return filter(list,isDeposit);
    }
    
    public List<Transaction> search(String accountNumber, String  dateA,String dateB,boolean isDeposit){
       List<Transaction> list=search(accountNumber, getDate(dateA),getDate(dateB));
       return filter(list,isDeposit);
    }
      
    
    public List<Transaction> searchLastMonth(String accountNumber){
        int[] result=lastMonth();
        String date1=result[0]+":"+intToString(result[1])+":01";
        String date2=result[0]+":"+intToString(result[1])+":"+result[2];
        return search(accountNumber, getDate(date1),getDate(date2));
    }
    
    public List<Transaction> searchLast7Day(String accountNumber){
        Date date=new Date();
        Date date1=new Date(date.getTime()-1440*60*7*1000);
        return search(accountNumber, date1,date);
    }
    
    
    public List<Transaction> search(String accountNumber, String  dateA,String dateB){
       return search(accountNumber, getDate(dateA),getDate(dateB));
    }
    
    private List<Transaction> search(String accountNumber, Date dateA,Date dateB) {
        Search search=new Search(ACCOUNTNUMBER,accountNumber);
        List<Transaction> transactions=(List<Transaction> ) (Object) search(search);
        return transactions.stream().filter(x->between(x,dateA,dateB)).collect(Collectors.toList());
    }
            
    private boolean   between(Transaction transaction,Date dateA,Date dateB) {
                Date da=transaction.getTransactionTs();
                return (da.getTime()>=dateA.getTime()) && 
                       (da.getTime()<=dateB.getTime());
            }
}
