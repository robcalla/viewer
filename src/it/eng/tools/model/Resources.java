package it.eng.tools.model;


public  class Resources {
	
	/*
	 * 
	"active": true,
	"id": "idm_project",
	"name": "idm"
	
	 */
	private boolean active;
	private String id;
	private String name;
	//private @JsonProperty("urn:scim:schemas:extension:keystone:2.0") UrnScimSchema urn;
	private UrnScimSchema urn;
	
	
	public UrnScimSchema getUrn() {
		return urn;
	}
	public void setUrn(UrnScimSchema urn) {
		this.urn = urn;
	}
	
	
	public boolean getActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}



	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}



	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}


	
	public Resources(boolean active, String id, String name, UrnScimSchema urn ) {
		this.active = active;
		this.id = id;
		this.name = name;
		this.urn = urn;
	}
	
	
	
}
