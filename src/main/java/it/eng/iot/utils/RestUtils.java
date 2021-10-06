package it.eng.iot.utils;

import it.eng.digitalenabler.restclient.DefaultRestClient;
import it.eng.digitalenabler.restclient.HTTPResponse;
import it.eng.iot.exceptions.ForbiddenException;
import it.eng.iot.exceptions.UnauthorizedException;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.core.MediaType;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import java.util.logging.*;

public abstract class RestUtils {
	
	private static final Logger LOGGER = Logger.getLogger(RestUtils.class.getName() );
	
	public static String consumePost(String url, Object body, Map<String, String> headers) 
			throws Exception{
		
		Client client = Client.create();
		Builder builder =  client.resource(url).type(MediaType.APPLICATION_JSON);
		
		if(headers != null){
			Set<Map.Entry<String, String>> hs = headers.entrySet();
			for(Map.Entry<String, String> h : hs){
				builder = builder.header(h.getKey(), h.getValue());
			}
		}
		
		ClientResponse resp =builder.post(ClientResponse.class, body.toString());
		
		if(resp.getStatus() == 401){
			throw new UnauthorizedException("URL "+url+
					" responded with status "+resp.getStatus());
		}
		
		if(resp.getStatus() == 403){
			throw new ForbiddenException("URL "+url+
					" responded with status "+resp.getStatus()+" "+resp.getStatusInfo().getReasonPhrase());
		}
		
		if(resp.getStatus()>301){
			throw new Exception("URL "+url+
					" responded with status "+resp.getStatus()+
					" and message: "+resp.getEntity(String.class));
		}
		
		String r = "";
		if(resp.getStatus()!= HttpStatus.SC_NO_CONTENT  && resp.hasEntity()){
			r = resp.getEntity(String.class);
		} else {
			try {resp.close();}
			catch(Exception e) {System.err.println("Cannot close connection");}
		}
		

		
		
		return r;
	}
	
	
	public static String consumeGet(String url) throws Exception{
		
		Client client = Client.create();
		ClientResponse resp  = client.resource(url).get(ClientResponse.class);
		
		if(resp.getStatus()>301){
			throw new Exception("URL "+url +
					" responded with status "+resp.getStatus() +
					" and message: "+resp.getEntity(String.class));
		}
		String r = "";
		if(resp.getStatus()!= HttpStatus.SC_NO_CONTENT  && resp.hasEntity()){
			r = resp.getEntity(String.class);
		} else {
			try {resp.close();}
			catch(Exception e) {System.err.println("Cannot close connection");}
		}
		
		return r;
	}
	
	public static String consumeGet(String url, HTTPBasicAuthFilter token) throws Exception{
		
		Client client = Client.create();
				client.addFilter(token);

		ClientResponse resp = client.resource(url).type(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		
		if(resp.getStatus()>301){
			throw new Exception("URL "+url +
					" responded with status "+resp.getStatus() +
					" and message: "+resp.getEntity(String.class));
		}
		
		String r = "";
		if(resp.getStatus()!= HttpStatus.SC_NO_CONTENT  && resp.hasEntity()){
			r = resp.getEntity(String.class);
		} else {
			try {resp.close();}
			catch(Exception e) {System.err.println("Cannot close connection");}
		}
		
		return r;
		
	}
	
	
	public static String consumeGet(String url, Map<String, String> headers) throws Exception{
		
		Client client = Client.create();
		Builder builder = client.resource(url).getRequestBuilder(); 
		
		if(headers != null){
			Set<Map.Entry<String, String>> hs = headers.entrySet();
			for(Map.Entry<String, String> h : hs){
				builder = builder.header(h.getKey(), h.getValue());
			}
		}
		
		ClientResponse resp = builder.get(ClientResponse.class);
		
		if(resp.getStatus()>301){
			throw new Exception("URL "+url +
					" responded with status "+resp.getStatus() +
					" and message: "+resp.getEntity(String.class));
		}
		
		String r = "";
		if(resp.getStatus()!= HttpStatus.SC_NO_CONTENT  && resp.hasEntity()){
			r = resp.getEntity(String.class);
		} else {
			try {resp.close();}
			catch(Exception e) {System.err.println("Cannot close connection");}
		}
		
		return r;
		
	}
	
	
	public static ClientResponse consumeDelete(String url, Map<String, String> headers) throws Exception{
		
		Client client = Client.create();
		Builder builder = client.resource(url).getRequestBuilder(); 
		if(headers != null){
			Set<Map.Entry<String, String>> hs = headers.entrySet();
			for(Map.Entry<String, String> h : hs){
				builder = builder.header(h.getKey(), h.getValue());
			}
		}
		ClientResponse resp = builder.delete(ClientResponse.class);
		
		if(resp.getStatus()>301){
			throw new Exception("URL "+url +
					" responded with status "+resp.getStatus() +
					" and message: "+resp.getEntity(String.class));
		} else {
			try {resp.close();}
			catch(Exception e) {System.err.println("Cannot close connection");}
		}
		
		return resp;
	}

	public static void consumePut (String url, Object body, MediaType type, Map<String, String> headers) throws Exception{
		
		Client client = Client.create();
		Builder builder =  client.resource(url).type(type);
		
		if(headers != null){
			Set<Map.Entry<String, String>> hs = headers.entrySet();
			for(Map.Entry<String, String> h : hs){
				builder = builder.header(h.getKey(), h.getValue());
			}
		}
		
		ClientResponse resp =builder.put(ClientResponse.class, body.toString());
		
		if(resp.getStatus()>301){
			throw new Exception("URL "+url+
					" responded with status "+resp.getStatus()+
					" and message: "+ resp.getEntity(String.class));
		} else {
			try {resp.close();}
			catch(Exception e) {System.err.println("Cannot close connection");}
		}
		
		return;
	}
	
	public static String consumePost (String url, Object body, MediaType type, Map<String, String> headers) throws Exception{
		
		Client client = Client.create();
		Builder builder =  client.resource(url).type(type);
		
		if(headers != null){
			Set<Map.Entry<String, String>> hs = headers.entrySet();
			for(Map.Entry<String, String> h : hs){
				builder = builder.header(h.getKey(), h.getValue());
			}
		}
		LOGGER.log(Level.INFO, "### Payload ### : "+body.toString());
		ClientResponse resp =builder.post(ClientResponse.class, body.toString());
		if(resp.getStatus()>301){
			throw new Exception("URL "+url+
					" responded with status "+resp.getStatus()+
					" and message: "+resp.getEntity(String.class));
		}
		
		String r = "";
		if(resp.getStatus()!= HttpStatus.SC_NO_CONTENT  && resp.hasEntity()){
			r = resp.getEntity(String.class);
		} else {
			try {resp.close();}
			catch(Exception e) {System.err.println("Cannot close connection");}
		}
		
		
		return r;
	}

	public static void consumePatch(String url, Object body, Map<String, String> headers) throws Exception{
		
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try{
			HttpPatch httpPatch = new HttpPatch(new URI(url));
		
			StringEntity entity = new StringEntity(body.toString());
			entity.setContentType("application/json");
			httpPatch.setEntity(entity);
		
			if(headers != null){
				Set<Map.Entry<String, String>> hs = headers.entrySet();
				for(Map.Entry<String, String> h : hs){
					httpPatch.setHeader(h.getKey(), h.getValue());
				}
			}
			
			CloseableHttpResponse response = httpClient.execute(httpPatch);
			try{
				int status = response.getStatusLine().getStatusCode();
				if(status>301){
					throw new Exception("URL "+url +
							" responded with status "+status);
				}
			}
			finally{
				response.close();
			}			
		}
		finally{
			httpClient.close();
		}
		
		return ;
	}

	
	// Return the full ClientResponse
	public static ClientResponse consumePostFull(String url, Object body, Map<String, String> headers) throws Exception{
		
		Client client = Client.create();
		Builder builder =  client.resource(url).type(MediaType.APPLICATION_JSON);
		
		if(headers != null){
			Set<Map.Entry<String, String>> hs = headers.entrySet();
			for(Map.Entry<String, String> h : hs){
				builder = builder.header(h.getKey(), h.getValue());
			}
		}
		
		ClientResponse resp =builder.post(ClientResponse.class, body.toString());
		
		if(resp.getStatus()>301){
			throw new Exception("URL "+url+
					" responded with status "+resp.getStatus()+
					" and message: "+resp.getEntity(String.class));
		} else {
			try {resp.close();}
			catch(Exception e) {System.err.println("Cannot close connection");}
		}
		

		return resp;
	}
	
	public static Integer validateSession (String endpoint, String token) throws Exception{		
		
		DefaultRestClient client = new DefaultRestClient();
		
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Authorization", "Bearer " + token);
		
		HTTPResponse response = client.consumeGet(endpoint, headers);        
        
		return response.getResponseCode();
				
	}
	
	
}
