package com.jm.controller;


import com.jm.mock.MockModel;
import com.jm.model.Account;
import com.jm.model.Result;
import com.jm.model.Search;
import com.jm.model.Transaction;
import com.jm.service.TransactionService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {
   
    @Autowired
    protected TransactionService transactionService;
    
        
    @RequestMapping(value = "getAllTransaction", method = RequestMethod.GET)
    public List<MockModel> getAllTransaction() {
        return transactionService.findAll();
    }
    
    @RequestMapping(value = "getCount", method = RequestMethod.GET)
    public int getCount() {
        return transactionService.findAll().size();
    }
    
    @RequestMapping(value = "searchTransaction", method = RequestMethod.POST)
    public List<Transaction> searchTransaction(@RequestBody Search search) {
        return (List<Transaction>)(Object) transactionService.search(search);
    }
    
    @RequestMapping(value = "deposit/{accountNumber}/{amount}", method = RequestMethod.GET)
    public Result deposit(      
        @PathVariable("accountNumber") String accountNumber,
        @PathVariable("amount") double amount){
        return  transactionService.deposit(accountNumber,amount);
            
    }
    
    @RequestMapping(value = "withdraw/{accountNumber}/{amount}", method = RequestMethod.GET)
    public Result withdraw(      
        @PathVariable("accountNumber") String accountNumber,
        @PathVariable("amount") double amount){
        return  transactionService.withdraw(accountNumber,amount);
     }
      
    
    @RequestMapping(value = "searchDate/{accountNumber}/{date1}/{date2}", method = RequestMethod.GET)
    public List<Transaction> searchPath(
                 @PathVariable("accountNumber") String accountNumber,
                 @PathVariable("date1") String date1,
                 @PathVariable("date2") String date2) {
        return (List<Transaction>)(Object) transactionService.search(accountNumber,date1,date2);
    }
    
    @RequestMapping(value = "searchLast7Day/{accountNumber}", method = RequestMethod.GET)
    public List<Transaction> searchLast7Day(
                 @PathVariable("accountNumber") String accountNumber) {
        return (List<Transaction>)(Object) transactionService.searchLast7Day(accountNumber);
    }
    
    @RequestMapping(value = "searchLastMonth/{accountNumber}", method = RequestMethod.GET)
    public List<Transaction> searchLastMonth(
                 @PathVariable("accountNumber") String accountNumber) {
        return (List<Transaction>)(Object) transactionService.searchLastMonth(accountNumber);
    }
    
    
    

    


}
