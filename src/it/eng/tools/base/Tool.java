package it.eng.tools.base;

import it.eng.iot.utils.CommonUtils;

import java.net.MalformedURLException;

import com.google.gson.Gson;

abstract class Tool {
	
	private String baseUrl;
	private Gson gson = new Gson();
	
	protected Tool(String baseurl) throws Exception{
		setBaseUrl(baseurl);
	}
	
	public String getBaseUrl() {
		return this.baseUrl;
	}

	private void setBaseUrl(String url) 
			throws Exception {
		
		if(url==null){
			throw new NullPointerException("ContextBroker URL is NULL");
		}
		String s = url.trim();
		if(!CommonUtils.isValidURL(s)){
			throw new MalformedURLException("ContextBroker URL is not valid");
		}
		
		this.baseUrl = url;
	}

	protected Gson getGson() {
		return gson;
	}

	protected void setGson(Gson gson) {
		this.gson = gson;
	}
}
