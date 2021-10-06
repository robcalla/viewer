package it.eng.iot.utils;

import java.io.File;
import java.net.URL;
import java.security.SecureRandom;

import org.apache.commons.lang3.text.WordUtils;


public abstract class CommonUtils {

	public static boolean isValidURL(String urlString){
	    boolean out = false;
		try{
	        URL url = new URL(urlString);
	        url.toURI();
	        out = true;
	    } 
	    catch (Exception exception){
	        out = false;
	    }
		
		return out;
	}
	
	public static String randomAlphanumericString(int length) {
		SecureRandom rnd = new SecureRandom();
		String values = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		StringBuilder sb = new StringBuilder(length);
		   for( int i = 0; i < length; i++ ) 
		      sb.append( values.charAt( rnd.nextInt(values.length()) ) );
		return sb.toString();
	}
	
	public static boolean deleteImageFromDir(File dir,String fileName) {	
		File[] list = dir.listFiles();
		if(list!=null)
		for (File fil : list)
		{
		    if (!fil.isDirectory())
		    {
		    	if (fil.getName().contains(fileName))
		        {
		    		fil.delete();
		    		return true;
		        }
		    }
		    
		}
		return false;
	}
	
}
