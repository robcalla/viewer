package it.eng.iot.servlet.model;

import java.io.Serializable;

public class RoleBean implements Serializable{

	private static final long serialVersionUID = 4561317526109329214L;
	private String name;
	private String id;
	
	public RoleBean(String name, String id) {
		this.name = name;
		this.id = id;
	}

	public RoleBean(){
		this.name = "";
		this.id = "";
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
