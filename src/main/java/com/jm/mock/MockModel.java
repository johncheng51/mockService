package com.jm.mock;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jm.util.Util;
import java.io.Serializable;
import java.lang.reflect.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class MockModel implements Comparable,Serializable {
    private static final String RTN = ",\r\n";
    private static final String COLN = ":";
    private static final String NULL = "'null'";
    private static final String QQ = "'";
    private static final String ABSMODEL = "absmodel";
    private int id;


    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    private transient String[] keys;
    private transient Mock mock = Mock.instance();
    private transient ThreadMgr threadMgr = ThreadMgr.instance();
    private static Map<String,Integer> mapInt=new Hashtable();
    public MockModel() {}

    public MockModel(String keyFields) {
        keys = Util.split(keyFields, ",");
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (keys == null)
            return super.equals(o);
        boolean yes = true;
        for (String key : keys) {
            Object value1 = getProp(key, o);
            Object value2 = getProp(key);
            yes &= value1.equals(value2);
        }

        return yes;
    }

    @Override
    public int hashCode() {
        if (keys == null)
            return super.hashCode();
        int result = 0;
        for (String key : keys)
            result += getProp(key).hashCode() * 29;
        return result;
    }


    protected String printColl(Stack<MockModel> stack, Object value) {
        StringBuilder sb = new StringBuilder();
        Collection coll = (Collection) value;
        for (Object object : coll)
            sb.append(printModel(stack, object));
        return sb + "";

    }

    protected String printModel(Stack<MockModel> stack, Object value) {
        StringBuilder sb = new StringBuilder();
        boolean isModel = value instanceof MockModel;
        if (!isModel) {
            sb.append(value + RTN);
            return sb + "";
        }
        MockModel model = mock.find(stack, value.getClass());
        if (model != null) {
            sb.append(value.getClass() + RTN);
            return sb + "";
        }
        model = (MockModel) value;
        stack.push(model);
        String result = model.print(stack);
        stack.pop();
        return result;
    }

    protected String getDate(Object object) {
        Date date = (Date) object;
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss");
        return sdf.format(date);
    }

    protected List<Field> getFields() {
        ArrayList list = new ArrayList();
        Field[] fields1 = getClass().getDeclaredFields();
        Field[] fields2 = null;
        Class myClass = getClass().getSuperclass();
        boolean isABS = myClass.equals(MockModel.class);
        if (!isABS)
            fields2 = myClass.getDeclaredFields();
        for (Field f : fields1)
            list.add(f);
        if (fields2 == null)
            return list;
        for (Field f : fields2)
            list.add(f);
        return list;
    }

    public String print(Stack<MockModel> stack) {
        List<Field> fields = getFields();
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getName() + "{\n");
        for (Field field : fields) {
            String name = field.getName();
            Object value = getProp(name);
            if (value == null) {
                sb.append(name + COLN + NULL + RTN);
                continue;
            }
            String type = mock.getType(field);
            switch (type) {
            case ABSMODEL:
                sb.append(name + COLN + printModel(stack, value));
                break;
            case "list":
                sb.append(name + COLN + printColl(stack, value));
                break;
            case "set":
                sb.append(name + COLN + printColl(stack, value));
                break;
            case "date":
                sb.append(name + COLN + QQ + getDate(value) + QQ + RTN);
                break;
            default:
                sb.append(name + COLN + QQ + value + QQ + RTN);
                break;
            }
        }
        sb.append("}\n");
        return sb + "";
    }

    public void load(Map map) {
        Field[] fields = getClass().getDeclaredFields();
        Stream.of(fields).forEach(f -> {
                String name = f.getName();
                String value = (String) map.get(name);
                setProp(f.getName(), value);
            });
    }

    @Override
    public String toString() {
        Stack<MockModel> stack = new Stack();
        stack.push(this);
        String result = print(stack);
        stack.pop();
        return result;
    }

    public void log(String message) {
        System.out.println(message);
    }

    public Object getProp(String field) {
        return getProp(field, this);
    }

    protected Object getProp(String field, Object value) {
        Object result = null;
        String cname = cap(true, field);
        try {
            Method m = value.getClass().getMethod(cname, new Class[] { });
            result = m.invoke(value, new Object[] { });
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    public void setProp(String field, Object value) {
        setProp(this, field, value);
    }

    public void setProp(Object obj, String field, Object value) {
        if (value == null)
            return;
        String cname = cap(false, field);
        try {
            Method method = findMethod(cname);
            method.invoke(obj, new Object[] { value });

        } catch (Exception e) {
            log("======================================================");
            log("Missing set" + cap(field) + "() in " + obj.getClass());
            log("======================================================");
        }
    }

    protected Method findMethod(String cname) {
        Class myClass = this.getClass();
        Method[] methods = myClass.getDeclaredMethods();
        for (Method m : methods) {
            if (m.getName().equals(cname))
                return m;
        }
        Class superc = getClass().getSuperclass();
        boolean isABS = myClass.equals(MockModel.class);
        if (isABS)
            return null;
        methods = superc.getDeclaredMethods();
        for (Method m : methods) {
            if (m.getName().equals(cname))
                return m;
        }
        return null;
    }

    protected String cap(boolean isGet, String method) {
        String verb = isGet ? "get" : "set";
        String ret = verb + Util.cap(method);
        return ret;
    }

    public int compareTo(Object o) {
        if (o == null)
            return 0;
        for (String key : keys) {
            int result = 0;
            Object value1 = getProp(key, o);
            Object value2 = getProp(key);
            if (value1 instanceof String)
                result = ((String) value1).compareToIgnoreCase(((String) value2));
            if (result != 0)
                return result;
        }
        return 0;
    }

    public MockModel copy() {

        try {
            MockModel model = getClass().newInstance();
            Field[] fa = getClass().getDeclaredFields();
            for (Field field : fa) {

                String name = field.getName();
                setProp(model, name, getProp(name));
            }
            return model;
        } catch (Exception e) {
        }
        return null;
    }

    public void mock() {
        mock(false);
    }

    public void mock(boolean onlyParent) {
        Stack stack = new Stack();
        stack.push(this);
        mock(stack, onlyParent);
        stack.pop();
    }

    protected boolean isChild() {
        try {
            getClass().getDeclaredField("isChild");
            return true;
        } catch (NoSuchFieldException e) {
            return false;
        }
    }

    public void mock(Stack<MockModel> stack) {
        mock(stack, false);
    }
    
    public boolean isTypeObject(Field field) {
           String type = mock.getType(field);
           switch (type.toLowerCase()) {
           case "string":
           case "double":
           case "float" :
           case "Long"  :
           case "long"  :
           case "int"   :
           case "boolean":
           case "date"  :return false;
           default:return true;
           }
      }
    
    public boolean isString(Field field){
        String type = mock.getType(field).toLowerCase();
        return type.equals("string");
    }

    public void mock(Stack<MockModel> stack, boolean onlyParent) {
        try {

            String className = getClass().getSimpleName().toLowerCase();
            List<Field> fields = getFields();
            for (Field field : fields) {
                String name = field.getName();
                if (onlyParent && isTypeObject(field)) continue;
                String values=info(name);
                Object value=null;
                value = (values==null)?  mock.mock(stack, className, field,true):
                         isString(field)?  mock.select(values):mock.selectDouble(values);
                if (value != null)  
                    setProp(name, getInc(value,name));
              
            }
            threadMgr.addModel(this);
        } catch (Exception e) {
            log(e.getMessage());
        }

    }
    
    private Object getInc(Object value,String key){
        if (value instanceof String) {
            String keys=incKeys();
            boolean exist=Util.isBlank(keys)? false:keys.contains(key);
            if (!exist) return value;
             Integer number=mapInt.get(key);
             if (number==null)  number=999;
             number+=1;
            mapInt.put(key,number);
            return value+":::"+number;
        }
        else return value;
    }
    
    public String info(String key)
      {
         String name=key+"Info";
         Object  result= getProp(name);
         if (result!=null) return result+"";
         else return null;
      }
    public String incKeys(){ return "";}

    protected String cap(String text) {
        return Util.cap(text);
    }

    @JsonIgnore
    public List<String> getAllNames() {
        return getFields().stream().map(x -> x.getName()).collect(Collectors.toList());
    }
}
