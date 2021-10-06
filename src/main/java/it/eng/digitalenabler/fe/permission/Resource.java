package it.eng.digitalenabler.fe.permission;

import it.eng.digitalenabler.fe.enumeration.ResourceEnum;

public class Resource {
	private ResourceEnum type;
	private String id;
	
	public Resource(ResourceEnum type, String id) {
		super();
		this.type = type;
		this.id = id;
	}

	public ResourceEnum getType() {
		return type;
	}

	public void setType(ResourceEnum type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	
}
