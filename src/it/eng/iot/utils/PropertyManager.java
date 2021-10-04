package it.eng.iot.utils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import it.eng.iot.configuration.Conf;
import it.eng.iot.configuration.ConfGrayLogger;
import it.eng.iot.configuration.ConfHelper;
import it.eng.iot.configuration.ConfIDM;
import it.eng.iot.configuration.ConfOrionCB;
import it.eng.iot.configuration.FrontendConf;

public class PropertyManager {

	private static ArrayList<String> confFiles = new ArrayList<String>(
			Arrays.asList("/it/eng/iot/configuration/frontend_configuration.properties"
			)
	);
	
	private static ArrayList<ConfHelper> confObj = new ArrayList<ConfHelper>();
	
	
	private static Properties properties = null;
	private static Set<Object> propsName = new HashSet<Object>();
	private static Map<String,String> props= new HashMap<String,String>();
	
	static {	
		confObj.add(FrontendConf.getInstance());
		
		properties = new Properties();
		try {
			for(int i=0;i<confFiles.size();i++) {
				properties.clear();
				propsName.clear();
				String fileName = confFiles.get(i);
				properties.load(PropertyManager.class.getClassLoader().getResourceAsStream(fileName));
				propsName.addAll(properties.keySet());
				
				for(Object s : propsName) {
					String pName = String.valueOf(s);
					String pValue = null;
					pValue = confObj.get(i).getString(pName);	
					props.put(pName, pValue);
				}
				
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getProperty(String propName) {
		return props.get(propName);
	}
	
	public static Map<String,String> getProperties(){
		return props;
	}

}
