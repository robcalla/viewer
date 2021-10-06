package it.eng.tools.base;

import it.eng.tools.model.CBEntity;

import java.util.Set;

import org.json.JSONObject;

public abstract class ContextBroker extends Tool {
	
	protected ContextBroker(String baseurl) throws Exception{
		super(baseurl);
	}
	
	protected abstract String getEntity(String fiwareservice, String fiwareservicepath, String entityid);
	
	protected abstract String getEntities(String fiwareservice, String fiwareservicepath);

	protected abstract JSONObject postEntities(String fiwareservice, String fiwareservicepath, Set<? extends CBEntity> entities);

}
