package com.jm.controller;


import com.jm.mock.MockModel;
import com.jm.model.Search;
import com.jm.model.User;
import com.jm.service.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {
   
    @Autowired
    protected UserService userService;
    

    @RequestMapping(value = "getAll", method = RequestMethod.GET)
    public List<MockModel> getAll() {
        return userService.findAll();
    }
    
    @CrossOrigin
    @RequestMapping(value = "getCount", method = RequestMethod.GET)
    public int getCount() {
        return userService.findAll().size();
    }
    
    @CrossOrigin
    @RequestMapping(value = "searchUser", method = RequestMethod.POST)
    public List<User> searchUser(@RequestBody Search search) {
        return (List<User>)(Object) userService.search(search);
    }
    
    @CrossOrigin
    @RequestMapping(value = "createUser", method = RequestMethod.POST)
    public User createUser(@RequestBody User user) {
        return (User) userService.create(user);
    }
    
    @CrossOrigin
    @RequestMapping(value = "saveUser", method = RequestMethod.POST)
    public User saveUser(@RequestBody User user) {
        return (User) userService.update(user);
    }

    @CrossOrigin
    @RequestMapping(value = "getUser", method = RequestMethod.GET)
    public User getUser() {
        User user=new User();
        user.mock();
        return user;
    }
    @RequestMapping(value = "searchPath/{key}/{value}", method = RequestMethod.GET)
    public List<User> searchParam(
                 @PathVariable("key") String key,
                 @PathVariable("value") String value) {
        Search search=new Search(key,value);
        return (List<User>)(Object) userService.search(search);
    }
    
    @CrossOrigin
    @RequestMapping(value = "searchRequest", method = RequestMethod.GET)
    public List<User> searchRequest(
                 @RequestParam("key") String key,
                 @RequestParam("value") String value) {
        Search search=new Search(key,value);
        return (List<User>)(Object) userService.search(search);
    }
    

    


}
