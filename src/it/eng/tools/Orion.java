package it.eng.tools;

import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.core.MediaType;

import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.sun.jersey.api.client.ClientResponse;

import it.eng.iot.configuration.ConfOrionCB;
import it.eng.iot.utils.RestUtils;
import it.eng.tools.base.ContextBroker;
import it.eng.tools.model.CBEntity;
import java.util.logging.*;

public class Orion extends ContextBroker {
	
	private static final Logger LOGGER = Logger.getLogger(Orion.class.getName() );
	
	private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.00'Z'");
	
	private String path_entities = ConfOrionCB.getInstance().getString("orion.v2.entities");
	private String path_subscribe = ConfOrionCB.getInstance().getString("orion.v2.subscriptions");
	
	static {
		LogFilter logFilter = new LogFilter();
		LOGGER.setFilter(logFilter);
	}
	
	public Orion() throws Exception{
		super(ConfOrionCB.getInstance().getString("orion.protocol") + "://" + ConfOrionCB.getInstance().getString("orion.host"));
	}

	public String getPathEntities(){
		return this.path_entities;
	}
	
	public String getPathSubscribe(){
		return this.path_subscribe;
	}
	
	public String dateFormat(Date date){
		return this.sdf.format(date);
	}
	
	public Date dateParse(String date) throws ParseException{
		return this.sdf.parse(date);
	}
	
	@Override
	public String getEntity(String fiwareservice, String fiwareservicepath, String entityid) {
		
		String servicepath = fiwareservicepath.startsWith("/") ? fiwareservicepath : "/"+fiwareservicepath;
		
		String resp = "";
		String url = getBaseUrl()+this.path_entities + "/" + entityid;
		
		Map<String, String> headers = new HashMap<String, String>();
						headers.put("fiware-service", fiwareservice);
						headers.put("fiware-servicepath", servicepath);
		
		try{ resp = RestUtils.consumeGet(url, headers); }
		catch(Exception e){
			e.printStackTrace();
		}
						
		return resp;
	}

	@Override
	public String getEntities(String fiwareservice, String fiwareservicepath) {
		
		String servicepath = fiwareservicepath.startsWith("/") ? fiwareservicepath : "/"+fiwareservicepath;
		
		String resp = "";
		String url = getBaseUrl()+this.path_entities+"?limit=1000";
		
		Map<String, String> headers = new HashMap<String, String>();
						headers.put("fiware-service", fiwareservice);
						headers.put("fiware-servicepath", servicepath);
		
		try{ resp = RestUtils.consumeGet(url, headers); }
		catch(Exception e){
			e.printStackTrace();
		}
						
		return resp;
	}
	
	public String getEntities(String fiwareservice, String fiwareservicepath, Set<BasicNameValuePair> queryParams) {
		
		BasicNameValuePair limitParam = new BasicNameValuePair("limit", "1000");
		queryParams.add(limitParam);
		String queryString = "?"+URLEncodedUtils.format(queryParams, Charset.defaultCharset());
		
		String servicepath = fiwareservicepath.startsWith("/") ? fiwareservicepath : "/"+fiwareservicepath;
		
		String resp = "";
		String url = getBaseUrl()+this.path_entities + queryString;
		LOGGER.log(Level.INFO, "Invoking url: " + url);
		Map<String, String> headers = new HashMap<String, String>();
						headers.put("fiware-service", fiwareservice);
						headers.put("fiware-servicepath", servicepath);
		
		try{ resp = RestUtils.consumeGet(url, headers); }
		catch(Exception e){
			e.printStackTrace();
		}
						
		return resp;
	}
	
	@Override
	public JSONObject postEntities(String fiwareservice, String fiwareservicepath, Set<? extends CBEntity> entities){
		
		JSONObject out = new JSONObject();
		
		for(CBEntity entity : entities){
			boolean esito = postEntity(fiwareservice, fiwareservicepath, entity);
			out.put(entity.getId(), esito);
		}
		
		return out;
	}
	
	private boolean postEntity(String fiwareservice, String fiwareservicepath, CBEntity entity){
		
		String servicepath = fiwareservicepath.startsWith("/") ? fiwareservicepath : "/"+fiwareservicepath;
		
		String url = getBaseUrl()+this.path_entities;
		Map<String, String> configHeaders = new HashMap<String, String>();
						configHeaders.put("fiware-service", fiwareservice);
						configHeaders.put("fiware-servicepath", servicepath);
		boolean out = true;
		try{ RestUtils.consumePost(url, getGson().toJson(entity), configHeaders); }
		catch(Exception e){
			e.printStackTrace();
			out = false;
		}
		
		return out;
	}
	
	public JSONObject postEntities(String fiwareservice, String fiwareservicepath, JSONArray entities){
		
		JSONObject out = new JSONObject();
		for(int i=0; i< entities.length(); i++){
			JSONObject j = entities.getJSONObject(i);
			boolean esito = postEntity(fiwareservice, fiwareservicepath, j);
			out.put(j.getString("id"), esito);
		}
		
		return out;
	}
	
	public boolean postEntity(String fiwareservice, String fiwareservicepath, JSONObject entity){
		
		String servicepath = fiwareservicepath.startsWith("/") ? fiwareservicepath : "/"+fiwareservicepath;
		
		String url = getBaseUrl()+this.path_entities;
		Map<String, String> configHeaders = new HashMap<String, String>();
						configHeaders.put("fiware-service", fiwareservice);
						configHeaders.put("fiware-servicepath", servicepath);
		boolean out = true;
		try{ RestUtils.consumePost(url, entity.toString(), configHeaders); }
		catch(Exception e){
			e.printStackTrace();
			out = false;
		}
		
		return out;
	}
	
	public boolean updateEntityAttributeWithType(String fiwareservice, String fiwareservicepath, String entityid, String entityType, String attr, Object newval){

		String servicepath = fiwareservicepath.startsWith("/") ? fiwareservicepath : "/"+fiwareservicepath;

		String url = getBaseUrl()+this.path_entities+"/"+entityid+"/attrs"+"?type="+entityType;
		Map<String, String> configHeaders = new HashMap<String, String>();
		configHeaders.put("fiware-service", fiwareservice);
		configHeaders.put("fiware-servicepath", servicepath);
		boolean out = true;
		try{
			JSONObject body = new JSONObject();
			
			JSONObject val = new JSONObject();
				val.put("value", newval);
				
			body.put(attr, val);
			
			JSONObject dateModified = new JSONObject();
				dateModified.put("value", this.dateFormat(GregorianCalendar.getInstance().getTime()));
				
			body.put("dateModified", dateModified);
			
			MediaType mediatype = MediaType.APPLICATION_JSON_TYPE;

			RestUtils.consumePost(url, body, mediatype, configHeaders); 
		}
		catch(Exception e){
			e.printStackTrace();
			out = false;
		}

		return out;
	}
	
	public boolean updateEntityAttribute(String fiwareservice, String fiwareservicepath, String entityid, String attr, Object newval){

		String servicepath = fiwareservicepath.startsWith("/") ? fiwareservicepath : "/"+fiwareservicepath;

		String url = getBaseUrl()+this.path_entities+"/"+entityid+"/attrs";
		Map<String, String> configHeaders = new HashMap<String, String>();
		configHeaders.put("fiware-service", fiwareservice);
		configHeaders.put("fiware-servicepath", servicepath);
		boolean out = true;
		try{
			JSONObject body = new JSONObject();
			
			JSONObject val = new JSONObject();
				val.put("value", newval);
				
			body.put(attr, val);
			
			JSONObject dateModified = new JSONObject();
				dateModified.put("value", this.dateFormat(GregorianCalendar.getInstance().getTime()));
				
			body.put("dateModified", dateModified);
			
			MediaType mediatype = MediaType.APPLICATION_JSON_TYPE;

			RestUtils.consumePost(url, body, mediatype, configHeaders); 
		}
		catch(Exception e){
			e.printStackTrace();
			out = false;
		}

		return out;
	}

	public boolean subscribe(String fiwareservice, String fiwareservicepath, String body) {
		
		String servicepath = fiwareservicepath.startsWith("/") ? fiwareservicepath : "/"+fiwareservicepath;
		
		String url = getBaseUrl() + path_subscribe;
		Map<String, String> headers = new HashMap<String, String>();
			headers.put("fiware-service", fiwareservice);
			headers.put("fiware-servicepath", servicepath);
		boolean out = true;
		try{ 
			LOGGER.log(Level.INFO, "in: "+body);
			String serviceResponse = RestUtils.consumePost(url, body, headers);
			LOGGER.log(Level.INFO, "out: "+serviceResponse);
		}
		catch(Exception e){
			e.printStackTrace();
			out = false;
		}
		return out;
	}
	
	public boolean deleteEntity(String fiwareservice, String fiwareservicepath, String entity_id) 
			throws Exception {
		
		String servicepath = fiwareservicepath.startsWith("/") ? fiwareservicepath : "/"+fiwareservicepath;
		
		String url = getBaseUrl() + path_entities + "/" + entity_id;
		
		Map<String, String> headers = new HashMap<String, String>();
							headers.put("fiware-service", fiwareservice);
							headers.put("fiware-servicepath", servicepath);

		ClientResponse cr = RestUtils.consumeDelete(url, headers);
		if(cr.getStatus() > 299)
			return false;
		
		return true;
	}
	
	public String getEntityAttributes(String fiwareservice, String fiwareservicepath, String entityid) {
		
		String servicepath = fiwareservicepath.startsWith("/") ? fiwareservicepath : "/"+fiwareservicepath;
		
		String resp = "";
		String url = getBaseUrl()+this.path_entities + "/" + entityid + "/attrs";
		
		Map<String, String> headers = new HashMap<String, String>();
						headers.put("fiware-service", fiwareservice);
						headers.put("fiware-servicepath", servicepath);
		
		try{ resp = RestUtils.consumeGet(url, headers); }
		catch(Exception e){
			e.printStackTrace();
		}
						
		return resp;
	}
	
	public boolean deleteEntityAttribute(String fiwareservice, String fiwareservicepath, String entityid, String attributename)
			throws Exception {
		
		String servicepath = fiwareservicepath.startsWith("/") ? fiwareservicepath : "/"+fiwareservicepath;
		
		String url = getBaseUrl() + path_entities + "/" + entityid + "/attrs/" + attributename;
		
		Map<String, String> headers = new HashMap<String, String>();
			headers.put("fiware-service", fiwareservice);
			headers.put("fiware-servicepath", servicepath);

			ClientResponse cr = RestUtils.consumeDelete(url, headers);
		
			LOGGER.log(Level.INFO, "\t" + cr.getStatus());
			LOGGER.log(Level.INFO, "===================\n");
		
		return true;
	}
	

}
