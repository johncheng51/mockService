package com.jm.service;
import com.jm.model.Lookup;
import com.jm.util.Util;
import com.jm.xml.XmlBlock;
import com.jm.xml.XmlBody;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;

import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import org.springframework.stereotype.Service;

@Service
public class LookupService {
    public static final String LOOKUP="lookup";
    public static final String LOOKUPTXT=LOOKUP+".txt";
    public static final String REV="rev";
    protected Map<String, List<Lookup>> currentMap = new Hashtable();
    @PostConstruct
    private void init() {
        loadLookup();
    }
       
    private void loadLookup(){
        String text=Util.readResource(LOOKUPTXT);
        Map<String,Map<String,XmlBody>> map=XmlBlock.getMap(text);
        Map<String,XmlBody> bmap=map.get(LOOKUP);
        bmap.forEach((x,y)->loadMap(x,y));
     }
    
    private void loadMap(String key,XmlBody xmlBody){
        String text=xmlBody.body();
        boolean isRev=xmlBody.get(REV).length()>1;
        Map<String,String> map=Util.readProp(text);
        List<Lookup> list=new ArrayList();
        map.forEach((x,y)->{
                    list.add(isRev? new Lookup(y,x): new Lookup(x,y));
        });
        currentMap.put(key,list);
    }

    public List<String> keys() {
        return currentMap.keySet().stream().collect(Collectors.toList());
    }

    public List<Lookup> getType(String type) {
        return currentMap.get(type);
    }
}
