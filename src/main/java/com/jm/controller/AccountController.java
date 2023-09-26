package com.jm.controller;


import com.jm.mock.MockModel;
import com.jm.model.Account;
import com.jm.model.Search;
import com.jm.service.AccountService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/account")
public class AccountController {
   
      
    @Autowired
    protected AccountService accountService;
    
    @RequestMapping(value = "getAllAccount", method = RequestMethod.GET)
    public List<MockModel> getAllAccount() {
        return accountService.findAll();
    }
    
    @RequestMapping(value = "getCount", method = RequestMethod.GET)
    public int getCount() {
        return accountService.findAll().size();
    }
    
    @RequestMapping(value = "searchAccount", method = RequestMethod.POST)
    public List<Account> searchUser(@RequestBody Search search) {
        return (List<Account>)(Object) accountService.search(search);
    }
    
    @RequestMapping(value = "createAccount", method = RequestMethod.POST)
    public Account createAccount(@RequestBody Account account) {
        return (Account) accountService.create(account);
    }
    
    @RequestMapping(value = "saveAccount", method = RequestMethod.POST)
    public Account saveUser(@RequestBody Account account) {
        return (Account) accountService.update(account);
    }

    @RequestMapping(value = "getAccount", method = RequestMethod.GET)
    public Account getAccount() {
        Account account=new Account();
        account.mock();
        return account;
    }
    
    @RequestMapping(value = "searchPath/{key}/{value}", method = RequestMethod.GET)
    public List<Account> searchParam(
                 @PathVariable("key") String key,
                 @PathVariable("value") String value) {
        Search search=new Search(key,value);
        return (List<Account>)(Object) accountService.search(search);
    }
    
    @RequestMapping(value = "searchRequest", method = RequestMethod.GET)
    public List<Account> searchRequest(
                 @RequestParam("key") String key,
                 @RequestParam("value") String value) {
        Search search=new Search(key,value);
        return (List<Account>)(Object) accountService.search(search);
    }
    

    


}
