package it.eng.tools.model;

public class ActionEntityBean {
	
	private String action;
	private String payload;
	
	public ActionEntityBean() {
		super();
	}

	public ActionEntityBean(String action, String payload) {
		super();
		this.action = action;
		this.payload = payload;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	@Override
	public String toString() {
		return " {\"action\":\"" + action + "\", \"payload\":\"" + payload + "\"}";
		
	}
	
	
	
	

}
