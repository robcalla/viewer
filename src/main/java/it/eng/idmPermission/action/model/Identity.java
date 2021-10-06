package it.eng.idmPermission.action.model;


import java.util.Set;

public class Identity{
	
	private Set<String> methods;
	private Password password;
	
	public Identity(Set<String> methods, Password password) {
		super();
		this.methods = methods;
		this.password = password;
	}
	public Set<String> getMethods() {
		return methods;
	}
	public void setMethods(Set<String> methods) {
		this.methods = methods;
	}
	public Password getPassword() {
		return password;
	}
	public void setPassword(Password password) {
		this.password = password;
	}
	
}
