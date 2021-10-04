package it.eng.digitalenabler.keycloak;

import java.util.HashSet;
import java.util.Set;

public class Role {
	
	private String id;
	private String name;
	private Set<Permission> permissions;
	
	public Role() {}
	
	public Role(String id,String name) {
		this(id,name,new HashSet<Permission>());
	}
	
	public Role(String id,String name, Set<Permission> permissions) {
		this.id = id;
		this.name = name;
		this.permissions = permissions;
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
	public Set<Permission> getPermissions() {
		return permissions;
	}
	public void setPermissions(Set<Permission> permissions) {
		this.permissions = permissions;
	}
	
	public void addPermission(Permission perm) {
		this.permissions.add(perm);
	}
	
	public void removePermission(Permission perm) {
		this.permissions.remove(perm);
	}

}
