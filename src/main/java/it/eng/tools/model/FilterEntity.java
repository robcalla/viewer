package it.eng.tools.model;

public  class FilterEntity extends CBEntity{
	
	private String scopeId;
	private String scopeName;
	private String urbanserviceId;
	private String urbanserviceName;
	private String device;
	private String attribute;
	private String name;
	
	public String getScopeId() {
		return scopeId;
	}

	public void setScopeId(String scopeId) {
		this.scopeId = scopeId;
	}
	
	public String getScopeName() {
		return scopeName;
	}

	public void setScopeName(String scopeName) {
		this.scopeName = scopeName;
	}

	public String getUrbanserviceId() {
		return urbanserviceId;
	}

	public void setUrbanserviceId(String urbanserviceId) {
		this.urbanserviceId = urbanserviceId;
	}
	public String getUrbanserviceName() {
		return urbanserviceName;
	}

	public void setUrbanserviceName(String urbanserviceName) {
		this.urbanserviceName = urbanserviceName;
	}
	

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	public FilterEntity() {
		super("","");
		this.scopeId = "";
		this.scopeName = "";
		this.urbanserviceId = "";
		this.urbanserviceName = "";
		this.device = "";
		this.attribute = "";
		this.name = "";
	}

	/**
	 * @param id
	 * @param type
	 * @param scope
	 * @param urbanservice
	 * @param device
	 * @param attribute
	 * @param name
	 */ 
	public FilterEntity(String id, String type, String scopeId, String scopeName, String urbanserviceId, String urbanserviceName,String device, String attribute, String name) {
		super(id, type);
		this.scopeId = scopeId;
		this.scopeName = scopeName;
		this.urbanserviceId = urbanserviceId;
		this.urbanserviceName = urbanserviceName;
		this.device = device;
		this.attribute = attribute;
		this.name = name;
	}
	
	
	
	
	
	
}
