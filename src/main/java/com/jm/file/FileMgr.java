package com.jm.file;


import com.jm.util.Util;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

public class FileMgr {
    private Map<String, String> mapFiles = new Hashtable();
    private String[] files;
    private static final String RTN = "\r\n";
    private static final String FILELIST = "filelist.txt";
    private void init() {
        String allFiles = resource(FILELIST);
        files = Util.split(allFiles, RTN);
        getMap();
        
    }

    private String resource(String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            
            InputStream inputStream = Util.readFResource(fileName);
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while ((line = in.readLine()) != null)
                stringBuilder.append(line + RTN);
            in.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }

    public List<String> listFiles() {
        return Arrays.asList(files);
    }


    private void getMap() {

        for (String file : files) {
            String loc = file + ".txt";
            mapFiles.put(file, resource(loc));
        }
    }


    public String readFile(String key) {
        return mapFiles.get(key);
    }


    private static FileMgr mgr;
    private static Object lock = new Object();

    public synchronized static FileMgr instance() {
        if (mgr != null)     return mgr;
        synchronized (lock) {
            mgr = new FileMgr();
            mgr.init();
            return mgr;
        }
    }

    private void log(String message) {
        System.out.println(message);
    }

}
