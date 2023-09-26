package com.jm.mock;

import com.jm.util.Util;
import java.util.ArrayList;
import java.util.List;

public class MockUtil {
    public static List<MockModel> create(int n, Class myClass,String primaryKey){
        String name=getClassName(myClass);
        List<MockModel> list=new ArrayList();
        for (int i=0;i<n;i++){
            MockModel model=(MockModel) Util.createObj(myClass);
            model.mock();
            Util.setProp(model, "id", i);
            Util.setProp(model, primaryKey, name+i);
            list.add(model);
            
        }
        return list;
    }
    
    public static String getClassName(Class myClass){
        return Util.getLast(myClass.getName(),".");
    }
}
