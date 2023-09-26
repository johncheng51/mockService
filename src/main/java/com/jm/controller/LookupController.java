package com.jm.controller;



import com.jm.model.Lookup;
import com.jm.service.LookupService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/lookup")
public class LookupController {
   
    @Autowired
    protected LookupService lookupService;
    @RequestMapping(value = "keys", method = RequestMethod.GET)
    public List<String> keys() {
        return lookupService.keys();
       
    }

    @RequestMapping(value = "{type}", method = RequestMethod.GET)
    public List<Lookup> find(@PathVariable("type") String type) {
        return lookupService.getType(type);
    }

    


}
