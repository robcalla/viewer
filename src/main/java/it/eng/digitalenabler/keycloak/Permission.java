package it.eng.digitalenabler.keycloak;

public class Permission {
	
	private String id;
	private String name;
	
	public Permission() {}

	public Permission(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
