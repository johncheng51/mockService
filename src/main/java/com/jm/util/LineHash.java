package com.jm.util;


import java.util.*;



public class LineHash extends LineProcess {
    private Hashtable<String, String> map = new Hashtable();
    
    public LineHash(String text) {
        super(text);
        process();
    }

    private void process() {
        while (true) {
            String key = getStart("$$_");
            if (Util.isBlank(key)) break;
            String value = getEnd("__end");
            map.put(key.trim(), value);
        }
    }
     
    

public static Map<String,String> getMap(String text){
    LineHash lineHash=new LineHash(text);
    return lineHash.map;
}

public static Map<String,String[]> getMapA(String text){
        Map<String,String[]> map=new Hashtable();
        LineHash lineHash=new LineHash(text);
        for (Object o:lineHash.map.keySet())
        {   String value=lineHash.map.get(o);
            String[] sa=Util.split(value, ","); 
            map.put((String) o, sa);                 
        }
        return map;
    }
}
