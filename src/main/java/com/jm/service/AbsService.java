package com.jm.service;

import com.jm.mock.MockModel;
import com.jm.model.Search;
import com.jm.util.Util;
import java.io.File;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;

public abstract class AbsService {
    public static final String DB = "dbfiles";
    public static final String ERRORSAME = "DB already exist with primaryKey [%s]";
    protected Map<Integer, MockModel> currentMap = new Hashtable();
    private File workFolder;
    protected String primaryKey;
    private boolean isDirty = false;
 

    public String getPrimaryKey() {return primaryKey;}
    protected int getMax() { return currentMap.keySet().stream().max(Integer::compare).get();}

    private String primaryKey(MockModel model) {
        return (String) Util.getProp(model, primaryKey);
    }

    private boolean exist(MockModel model) {
        return currentMap.values().stream().filter(x -> same(x, model)).findFirst().isPresent();
    }

    private boolean same(MockModel modela, MockModel modelb) {
        String a = primaryKey(modela);
        String b = primaryKey(modelb);
        return a.equals(b);
    }

    @PostConstruct
    private void init() {
        setPrimaryKey();
        workFolder = new File(Util.getWorkDir(), DB);
        if (!workFolder.exists())
            workFolder.mkdir();
        File file = new File(workFolder, getFileName());
        if (!file.exists()) {
            currentMap = loadObjects().stream().collect(Collectors.toMap(MockModel::getId, x -> x));
            String text = Util.writeObject(currentMap);
            Util.writeFile(file, text);
        } else {
            String text = Util.readFile(file);
            currentMap = (Map<Integer, MockModel>) Util.readObject(text);
        }

        Runnable r = () -> doSchedule();
        Thread thread = new Thread(r);
        thread.start();
    }

    private void doSchedule() {
        while (true) {
            doProcess();
            sleep(10);
        }
    }


    public List<MockModel> findAll() {
        return currentMap.values().stream().collect(Collectors.toList());
    }

    public MockModel create(MockModel model) {
        int n = getMax() + 1;
        if (exist(model))
            throw new RuntimeException(String.format(ERRORSAME, primaryKey(model)));
        model.setId(n);
        currentMap.put(n, model);
        isDirty = true;
        return model;
    }

    public MockModel update(MockModel model) {
        currentMap.put(model.getId(),model);
        isDirty = true;
        return model;
    }

    private synchronized void doProcess() {
        if (isDirty) {
            File file = new File(workFolder, getFileName());
            String text = Util.writeObject(currentMap);
            Util.writeFile(file, text);
        }
        isDirty=false;
    }

    private void sleep(int i) {
        try {
            Thread.currentThread().sleep(i * 1000);
        } catch (InterruptedException e) {
        }
    }
    
    public List<MockModel> search(Search search) {
       String key=search.getKey();
       String value=search.getValue();
       return currentMap.values().stream().filter(x->match(x,key,value))
              .collect(Collectors.toList());
    }
    
    private boolean match(MockModel model, String key, String value) {
        String pvalue=Util.getProp(model, key)+"";
        return pvalue.toLowerCase().indexOf(value.toLowerCase())>=0;
    }
    
    protected RuntimeException error(String format,String value){
        return new RuntimeException(String.format(format,value));
    }

    
    protected Date getDate(String date)  {
        String ERROR="Date input[%s] should be yyyy:mm:DD";
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy:mm:DD");
        try {
            Date newDate= sdf.parse(date);
            return newDate;
        } catch (ParseException e) {
            throw error(ERROR,date);
        }
    }
    
    protected double round(double d){
        int n=(int) d;
        double d1= n;
        return d1/10.0;
    }
    
    protected boolean isOdd(double d){
        int n=(int) d;
        return n%2==0;
    }
    
    protected abstract void setPrimaryKey();
    protected abstract List<MockModel> loadObjects();
    protected abstract String getFileName();

    
}
