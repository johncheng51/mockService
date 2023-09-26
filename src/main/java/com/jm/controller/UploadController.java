package com.jm.controller;


import com.jm.util.Util;

import java.io.File;

import java.io.IOException;

import java.io.InputStream;

import org.apache.tomcat.util.http.fileupload.IOUtils;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/upload")
public class UploadController {

    public static final String UPLOAD = "upload";
    @RequestMapping(value = "", method = RequestMethod.POST)
    public String handleFileUpload(
      @RequestParam("file") MultipartFile file) {
        try {
            writeFile(file.getOriginalFilename(),file.getBytes());
        }
        catch(Exception e){
            e.printStackTrace();
        }
      return file.getOriginalFilename();  
    }
    
    @RequestMapping(value = "getFile/{file}",
    method = RequestMethod.GET,
    produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody byte[] downLoad(
        @PathVariable("file") String file)  {
        return readFile(file);
    }
        
    private byte[] readFile(String fileName){
        File target=getFile(fileName);
        return Util.readFileByte(target);
    }
        
    private void writeFile(String fileName,byte[] data) {        
        File target=getFile(fileName);
        Util.writeFile(target, data);
    }
    
    private File getFile(String fileName){
        File workFolder = new File(Util.getWorkDir(), UPLOAD);
        String ext=Util.getLast(fileName, ".");
        if (!workFolder.exists())  workFolder.mkdir();
        workFolder=new File(workFolder,ext);
        if (!workFolder.exists())  workFolder.mkdir();
        File target=new File(workFolder,fileName);
        return target;
    }
}
