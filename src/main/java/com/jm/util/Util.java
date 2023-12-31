package com.jm.util;

import com.jm.xml.ReadHash;

import com.jm.xml.ReadProp;

import java.io.*;
import java.lang.reflect.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;


public class Util 
{
   private static String dot=".";	
   
   
   public static boolean isNumber(String s)
	    {
            if (isBlank(s)) return false;
	    	s=trim(s);
	    	if (s=="-") return false;
	    	for (int i=0;i<s.length();i++)
	    	{
                char  c = s.charAt(0);
	    		if (c=='.'|| c=='-') continue;
	    		if (c>='0'&& c<='9') continue;
	    		return false;
	    	}
	    	return true;
	    }	
   
   public static Hashtable readHash(String str)
   {   
   	   if (isBlank(str)) return new Hashtable();
       ReadHash rh=new ReadHash(str);
       return rh.getTable();
   }
   
   public static Hashtable readProp(String str)
   {   
   	   if (isBlank(str)) return new Hashtable();
       ReadProp rh=new ReadProp(str);
       return rh.getTable();
   }
   
   public static String  removeLast(String s,int n) 
   {   if (s == null || s.length() == 0)  return s;
   return s.substring(0, s.length() - n);
}
   
   public static String[] split(String str,String pat)
    {	   
      if (str== null) return new String[] {""};
            StringTokenizer st = new StringTokenizer(str, pat);
            String sa[] = new String[st.countTokens()];
            int i = 0;
            while (st.hasMoreElements())
                sa[i++] = ((String)st.nextElement()).trim();
            return sa;
        
    }
    
   

    public static String trimLF(String text)
    {
        if (text == null) return "";
       
        for (int i = 0; i < text.length(); i++)
        {
            char c=text.charAt(i);
            if (c == ' ' || c == '\r' || c == '\n' || c == 't') continue;
            return text.substring(i);
        }
        return null;
    }

    public static String mergeDot(String[] a)
   {   	 return a[0]+dot+a[1];   }

    public static String mergeRest(String text, String[] a)
   {   	 	
   	 String re=text;
     for (int i=2;i<a.length;i++)
	    	re+=dot+a[i];
	 return re;   	   
   }
   
   public static String  getPrefix(String s,int n,String patt) 
   {
       String result = "";
       String  data = s;
       for (int i=0; i < n; i++) {
           int nn = data.indexOf(patt);
           if (nn < 0)
               return result;
           result += data.substring(0, nn + patt.length());
           data = data.substring(nn + patt.length());
       }
       result = removeLast(result, 1);
       return result;
   }
   
   public static boolean  isBlank(String str) 
      {  
      	if (str==null) return true;
      	str=trim(str).toLowerCase();
        return str.length()==0 || str=="blank"; 
        }

    public static String[] Keys(Hashtable table)
    {
        String[] re=new String[table.size()];
        table.keySet().toArray(re);
        return re;
    }
        
   public static boolean isSpace(char str)
   {
   	return str=='\n' || 
   	       str==' '  || 
   	       str=='\t' || 
   	       str=='\r';
   }  
   
   public static boolean isWhite(String str)
   {
   	boolean flag=true;
   	for (int i=0;i<str.length();i++) flag = flag && isSpace(str.charAt(i));
   	return flag;
   }
   
   public static String removeQ(String str)
   {
   	if (isBlank(str))  return str;
        char c=str.charAt(0);
   	if (!(c=='\'' || c=='"')) return str;
   	str=str.substring(1);
        str=removeLast(str,1);
   	return str;
   }   
   
   public static String getLeft(String str,String pat)
   {       int n =str.lastIndexOf(pat);
   	       String result=str.substring(0,n);
   	       return result;        }
   	       
   public static String getLast(String str,String pat)
   {       
        	if (str==null) return "";
           int n =str.lastIndexOf(pat);
   	       String result=str.substring(n+pat.length());
   	       return result;        
   	}	       
   	
   public static String getFirst(String str,String pat)
   {       
   	       if (str==null) return "";
           int n =str.indexOf(pat);
           if (n<0) return "";
   	       String result=str.substring(0,n);
   	       return result;        
   } 
   
      
   public static boolean haveDot(String str)
   {   	return str.indexOf(dot)>0;   }
   
   public static String[] splitDot(String str)
   { return split(str,dot); } 
   
   public static String getRest(String str,String pat)
   {       int n =str.indexOf(pat);
           if (n<0) return str;
   	       String result=str.substring(n+1);
   	       return result;        
   }       
   	          
     
   
   public static char  findCommand(String table,String pattern)
   {
       if (table == null || pattern == null) return NC.CEND;
   	   table=table.toLowerCase()+" ";
   	   pattern=pattern.toLowerCase();
       if (pattern.length() < 2)  return NC.CEND;
       int index = table.indexOf(" " + pattern+" ");
       if (index == -1)  return NC.CEND;
       index -= 1;
       char a=table.charAt(index);
       index -= 1;
       char b = index >= 0 ? table.charAt(index) : ' ';
       if (b==' ') return a;
       else return b;
   }

    public static char findMatch(String[] table, String whole)
    {
        for (int i = 0; i < table.length / 2; i++)
        {
            String a = table[2*i + 1].toLowerCase();
            if (whole.toLowerCase().indexOf(a)>0) return table[2*i].charAt(0);
        }
        return (char) 0;
    }

    public static String[] getLines(String str)
   {   Lines lines=new Lines(str);
	   return lines.getLines();   }
	   
   public static String removeExtra(String str)
   {  
       String re="";
       String[] lines = getLines(str);
   	  for (String line: lines)
   	  {
   	  	if (isBlank(line)) continue;
   	  	re+=str+"\n";
   	  }
   	  return re;
   }
   
   
   private static String curDir=null;
   public static void setWorkDir(String file) 
   {     curDir=file;  }
   public static String getWorkDir()
   {
       if (curDir!=null) return curDir;
       String file = System.getProperty("user.dir");
       return file;
   }
   
    public static String getContent(File file) {
        try 
        {
            InputStream is = new FileInputStream(file);
            return getContent(is);
        } catch (Exception e) 
        {
            //logger.error(e.getMessage(),e);
            return null;
        }
    }
    
    
    public static String getContent(InputStream is) 
    {
        try {
            BufferedReader dis = new BufferedReader(new InputStreamReader(is));
            StringBuffer sb = new StringBuffer();
            String inputLine = null;
            while ((inputLine = dis.readLine()) != null)
                sb.append(inputLine+"\n");
            return sb + "";
        } catch (Exception e) 
        {
            return null;
        }
    }

   
   public static String trim(String str)
   {
       return str.trim(); 
   }
   
      public static String toPattern(String str)
      {
      	String re="";
      	boolean isEmpty=true;
      	for (int i=0;i<str.length();i++)
      		{
      		  if (str.charAt(i)==' ') re+=".";
      		  else { isEmpty=false;re+="X";}
      		}
      	if (isEmpty) return "";
      	else return re;
      }   
   
      public static boolean endWith(String a,String b)
      {
      	if (isBlank(a) || isBlank(b)) return false;
      	int n=a.toLowerCase().indexOf(b.toLowerCase());
      	if (n<0) return false;
      	return n==(a.length()-b.length());
      }
      
      public static boolean startWith(String a,String b)
      {    
         if (isBlank(a) || isBlank(b)) return false;
         a=trim(a).toLowerCase();
         b=trim(b).toLowerCase();	
         int n=a.indexOf(b);
         int n1=b.indexOf(a);
         return n==0 || n1==0;      
      }
      
      public static String cap(String str)
      { if (isBlank(str))     return "Null";
       	return (""+str.charAt(0)).toUpperCase()+str.substring(1);   }
      
      public static boolean eq(String a,String b)
      {
      	if (isBlank(a) || isBlank(b)) return false;
      	else return a.toLowerCase().equals(a.toLowerCase());
      }

    public static boolean have(String[] a, String b)
      {     for (String key:a) if (b.equals(key)) return true;
      return false;
  }
      
      public static String removeReturn(String s)
     { 
        char  c;
        String result= "";
        for (int i= 0; i < s.length(); i++) 
       {
         c = s.charAt(i);
         if (c == '\n' || c == '\r' || c == '\t')    continue;
         result += c;
        }
        return result;
      }
      
       public static String removeCR(String s)
     { 
        char c;
        String result= "";
        for (int i= 0; i < s.length(); i++) 
       {
         c = s.charAt(i);
         if (c == '\r' || c == '\t')    continue;
         result += c;
        }
        return result;
      }

    public static String printf(String template, String[] paramsx)
{	return printf0(template,"%",paramsx);  }      
    
public  static String printfWithMark(String template,
                                     String paramsx,String mark)
{
  if (isBlank(paramsx)) return template;
  return printf0(template, mark, new String[] { paramsx });
}

    public static String printf0(String template, String mark, String[] paramsx)
{
	if (paramsx.length==0) return template;
	int nn;int on;int next;	
	String result= template;
	String sub= null;
	while (true) 
   {
    on = result.indexOf(mark);
    if (on == -1)    break;
    next=on + mark.length();
    nn = result.charAt(next)-'0';
    sub = paramsx.length < (nn + 1) ? "" : paramsx[nn];
    result = result.substring(0, on) + sub + result.substring(next+1);
    }
  return result;   
}

public static boolean isIn(String text,String c)
{	return text.indexOf(c)>=0; }


  

    
public static Object getProp(Object self, String name)
        {  try {
            
        
            String cname="get"+cap(name);
            Method m =  self.getClass().getMethod(cname, new Class[] {  });
            return m.invoke(self, new Object[] { });
        }
        catch(Exception e) {e.printStackTrace();return null;}
        }

    public static String[] getFields(Object self)
    {
        Class  type= self.getClass();
        Field[] fa = type.getDeclaredFields();
        String[] result=new String[fa.length];
        for (int i = 0; i < fa.length; i++)
            result[i] = fa[i].getName();
        return result;
    }

    public static void setProp(Object self, String name, Object value)
    {
       if (value==null) return;
       Class arg=value.getClass();
       try
       {
        Class argc=arg==Integer.class? int.class:arg;
        String cname="set"+cap(name);
        Method m =  self.getClass().getMethod(cname, new Class[] { argc });
        m.invoke(self, new Object[] { value });
       }
       catch(Exception e) 
       {
            //e.printStackTrace();
       }
    }
    
private static Method findMethod(Object self,String name) 
{
    Method[] ma=self.getClass().getMethods();
    for (Method m:ma)
    if (m.getName().equals(name)) return m;
    return null;
}
    
public static Object executeMethod(Object self, String name, Object[] agrs)
   {  
     Method m=findMethod(self,name);
     try{ if (m==null) throw new Exception("Can not find method="+name+ "class="+self.getClass());    
          return m.invoke(self, agrs);     }
                catch(Exception e) 
                {
                     e.printStackTrace();
                     return null;
                }
             }

   


    
    
public static Object StringToObj(Object obj) 
{
    if (!(obj instanceof String)) return obj;
    String val=(String) obj;
    if (isNumber(val)) return Integer.parseInt(val);
    return val;
}


    
public static Object createObj(String className) 
{
   try {
    Class myClass=Class.forName(className);
    Object re=myClass.newInstance();return re;
   }
   catch(Exception e) {e.printStackTrace();return null;}
}

    public static Object createObj(Class  myClass) 
    {
       try {
        
        Object re=myClass.newInstance();return re;
       }
       catch(Exception e) {e.printStackTrace();return null;}
    }

    public static byte[] readFileByte(String file) {
        try {
            InputStream is = new FileInputStream(file);
            return readBytes(is);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static byte[] readFileByte(File file) {
        try {
            InputStream is = new FileInputStream(file);
            return readBytes(is);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] readBytes(InputStream is) {
        try {
            BufferedInputStream bis = (new BufferedInputStream(is));
            DataInputStream dis = new DataInputStream(bis);
            int n = dis.available();
            byte[] result = new byte[n];
            dis.readFully(result);
            dis.close();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String readFile(File file) {
        return readFile(file.getAbsolutePath());
    }
    
    public static String readFile(String file) {
        try {
            InputStream is = new FileInputStream(file);
            byte[] result = readBytes(is);
            is.close();
            return new String(result);
        } catch (Exception e) {
           throw new RuntimeException(e);
        }
    }
    
    public static File writeFile(File file, String data) {
        log("Write File:" + file);
        return writeFile(file.getAbsolutePath(), data.getBytes());
    }

    public static File writeFile(File file, byte[] data) {
        return writeFile(file.getAbsolutePath(), data);
    }

    public static File writeFile(String fileName, byte[] data) {
        makeFolder(fileName);
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            fos.write(data);
            fos.close();
            return new File(fileName);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void makeFolder(String fileName) {
        String newFile = "";
        String[] sa = split(fileName, "\\");
        for (String s : sa) {
            if (s.indexOf(".") >= 0)
                break;
            newFile += s + "\\";
            File f = new File(newFile);
            if (!f.exists())
                f.mkdir();
        }
    }

    public static void writeFile(String folder, String file, String data) {
        makeFolder(folder);
        File myFile = new File(folder, file);
        writeFile(myFile, data);
    }
    
    private static void log(String message){
        System.out.println(message);
    }
    
   
       public static List<Class> getClasses(String packageName)
                {
           try{  
           ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
           String path = packageName.replace('.', '/');
           Enumeration resources = classLoader.getResources(path);
           List<File> dirs = new ArrayList();
           while (resources.hasMoreElements()) {
               URL resource = (URL) resources.nextElement();
               dirs.add(new File(resource.getFile()));
           }
           ArrayList classes = new ArrayList();
           for (File directory : dirs) {
               classes.addAll(findClasses(directory, packageName));
           }
           return classes;
           }
           catch(Exception e){
               e.printStackTrace();
               return null;
           }
       }
       
    private static List findClasses(File directory, 
                                    String packageName) throws ClassNotFoundException {
           List classes = new ArrayList();
           if (!directory.exists()) {
               return classes;
           }
           File[] files = directory.listFiles();
           for (File file : files) {
               if (file.isDirectory()) {
                   assert !file.getName().contains(".");
                   classes.addAll(findClasses(file, packageName + "." + file.getName()));
               } else if (file.getName().endsWith(".class")) {
                   classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
               }
           }
           return classes;
       }
    
    public static PrintWriter getPrinter(String output) {
        try {
            FileOutputStream fos = new FileOutputStream(output,true);
            PrintWriter pw = new PrintWriter(fos);
            return pw;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void log(String output,String text){
        PrintWriter pw=getPrinter(output);
        SimpleDateFormat sdf=new SimpleDateFormat("MM-dd-yyyy hh:mm:ss");
        Date date=new Date();
        String dateTxt=sdf.format(date);
        pw.println(dateTxt+":"+text);
        pw.close();
        pw=null;
    }
    
    /** Read the object from Base64 string. */
      public static Object readObject( String text )  {
        try{
           byte [] data = Base64.getDecoder().decode( text );
           ObjectInputStream objectInputStream = new ObjectInputStream( 
                                           new ByteArrayInputStream(  data ) );
           Object object  = objectInputStream.readObject();
           objectInputStream.close();
           return object;}
        catch(Exception e){
            e.printStackTrace();

            return null;
        }
      }

       /** Write the object to a Base64 string. */
       public static String writeObject( Object object ) {
           try{
           ByteArrayOutputStream baos = new ByteArrayOutputStream();
           ObjectOutputStream oos = new ObjectOutputStream( baos );
           oos.writeObject( object );
           oos.close();
           return Base64.getEncoder().encodeToString(baos.toByteArray()); 
           }
           catch(Exception e){
               e.printStackTrace();
               return null;
           }
       }
       
    public static String readResource(String fileName) {
        Resource resource = new ClassPathResource("db/"+fileName);
        InputStream inputStream=null;
        try {
            inputStream = resource.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return readURL(inputStream);
    }
    
    public static InputStream readFResource(String fileName) {
        Resource resource = new ClassPathResource("file/"+fileName);
        InputStream inputStream=null;
        try {
            inputStream = resource.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputStream;
    }
    
    public static String readURL(InputStream inputStream) {
        StringBuilder sb = new StringBuilder();
        String resource;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            while ((resource = br.readLine()) != null)
                sb.append(resource + "\r\n");
            return sb + "";
        } catch (Exception e) {
            return null;
        }
    }
    
}  
  











