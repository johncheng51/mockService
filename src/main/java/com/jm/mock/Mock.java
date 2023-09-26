package com.jm.mock;

import com.jm.file.*;
import com.jm.util.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Stream;

public class Mock {
    private Map<String, Map<String, String[]>> curMap = new Hashtable();
    private Map<String, String[]> allMap;
    private FileMgr fileMgr = FileMgr.instance();
    private static final String ALL       = "all";
    private static final String NOTFOUND  = "notfound";
    private static final String ABSMODEL  ="mockmodel";
    private static final String DOT=".";
    private static final String COMMON=",";
    private Mock() {}

    private void init() {
        List<String> files = fileMgr.listFiles();
        files.forEach(file -> {
                String text = fileMgr.readFile(file);
                String name = file;
                curMap.put(name, LineHash.getMapA(text));
            });
        allMap=curMap.get(ALL);
    }

    private String find(String className, String key) {
        className=getBestMatch(className);
        Map<String, String[]> map1 = curMap.get(className);
        map1 =(map1==null)? allMap:map1;
        String[] sa = getBestMatch(key,map1);
        return random(sa);
    }
    
    private String[] getBestMatch(String key1,Map<String, String[]> map){
        final String key2=key1.toLowerCase();
        String result=null;
        try {
            result=(String) Stream.of(map.keySet().toArray()).filter(
           key->{
           String k=key+"";
           return (key2.indexOf(k)>=0) || (k.indexOf(key2+"")>=0);
           }
           ).findFirst().get();}
        catch(Exception e){}
        if (result!=null) return map.get(result);
        else return map.get(NOTFOUND);
    }
    
    private String getBestMatch(String text){
        ArrayList<String> list=new ArrayList();
        curMap.forEach((key,value)->{
             if (text.indexOf(key)>=0) list.add(key);           
        });
        return list.size()==0? text:list.get(0);    
    }

    private String random(String[] sa) {
        if (sa==null) return "NONE";
        double ram = Math.random();
        int n = (int) (ram * sa.length);
        return sa[n];
    }
    
    private String last(String text){
        return Util.getLast(text,".");
    }
    
    private Object mockMap(Stack<MockModel> stack,Field field) {
        ParameterizedType type = (ParameterizedType) field.getGenericType();
        Class<?> keyClass = (Class<?>) type.getActualTypeArguments()[0];
        Class<?> valueClass = (Class<?>) type.getActualTypeArguments()[1];
        Map coll=new HashMap();
        coll.put(make(stack,keyClass),make(stack,valueClass));
        coll.put(make(stack,keyClass),make(stack,valueClass));
        coll.put(make(stack,keyClass),make(stack,valueClass));
        return coll;
    }

    private Object mockList(Stack<MockModel> stack,Field field,boolean isSet) {

        ParameterizedType type = (ParameterizedType) field.getGenericType();
        Class<?> myClass = (Class<?>) type.getActualTypeArguments()[0];
        Collection coll=isSet? new HashSet():new ArrayList();
        coll.add(make(stack,myClass));
        coll.add(make(stack,myClass));
        coll.add(make(stack,myClass));
        return coll;
    }

    private Object make(Stack<MockModel> stack,Class<?> myClass) {
        String type=last(myClass+"");
        switch(type.toLowerCase())
        {
         case "string": return find("all",type);
         case "double":
         case "float" :return mockFloat();
            case "Long"  :
            case "long"  :return mockLong();
            case "int"   :return mockInt();
            case "date"  :return new Date();
        }
        try 
        {
            MockModel object=find(stack,myClass);
            if (object!=null) return  object;
            MockModel model = (MockModel) myClass.newInstance();
            model.mock(stack);
            return model;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public MockModel find(Stack<MockModel> stack,Class myClass){
        Optional<MockModel> option=  stack.stream().
               filter((x)->x.getClass().equals(myClass)).findFirst();
        if (option.isPresent()) return option.get();
        else return null;
    }
    
    private Float mockFloat() {
        double ram = Math.random();
        int n=(int)(ram*10000000);
        float ram1=(float) (n/100);
        return ram1;
    }
    
    private Integer mockInt(){
        double ram = Math.random();
        int n=(int) (ram*100);
        return n;
    }
    
    private Long mockLong(){
        double ram = Math.random();
        long n=(long) (ram*100);
        return n;
    }
    
    private Double mockRange(double a,double b,int count){
        double ram = Math.random();
        int n=(int) (ram*count);
        return a+(b-a)/count*n;
    }

    public Object mock(Stack<MockModel> stack,String className,Field field,boolean skip) {
           Object result=null;
           String name = field.getName();
           String type = Util.getLast(getType(field), DOT);
           switch (type.toLowerCase()) {
           case "string":result  =find(className,name);break;
           case "double":
           case "float" :result  =mockFloat();break;
           case "Long"  :
           case "long"  :result  =mockLong();break;
           case "int"   :result  =mockInt();break;
           case "date"  :result  =new Date();break;
           case "list"  :result  =mockList(stack,field,false);break;
           case "set"   :result  =mockList(stack,field,true);break;
           case "map"   :result  =mockMap(stack,field);break;
           case "boolean": result=false;break;
           case ABSMODEL: result=make(stack,field.getType());break;
           
           }
        return result;
    }
    
    public String getType(Field field) {
        Class myClass=field.getType();
        myClass=((myClass.getSuperclass()+"").toLowerCase().indexOf(ABSMODEL)>0)? 
                myClass.getSuperclass():myClass;
           String type = myClass + "";
            type = Util.getLast(type, DOT).toLowerCase();
        return type;
    }
    

    private static Object lock = new Object();
    private static Mock mgr;

    public synchronized static Mock instance() {
        if (mgr != null) return mgr;
        synchronized (lock) {
            mgr = new Mock();
            mgr.init();
            return mgr;
        }
    }

    public Object select(String values) {
      String[] words=Util.split(values,COMMON);
      return random(words);
    }
    
    public Object selectDouble(String values) {
      String[] words=Util.split(values,COMMON);
      double first=Double.parseDouble(words[0]);
      double second=Double.parseDouble(words[1]);
      int count=Integer.parseInt(words[2]);
      return  mockRange(first,second,count);
    }
}
