package it.eng.digitalenabler.keycloak;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class User {

	private String id;
	private String username;
	private String email;
	private Optional<Set<String>> roles;
	private Optional<Set<String>> organizations;
	private Optional<Set<String>> groups;
	
	public User() {}
	
	public User(String username, String email) {
		this(username,username,email);
	}

	public User(String id, String username, String email) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.roles = Optional.of(new HashSet<String>());
	}

	public User(String id, String username, String email, Set<String> roles) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.roles = Optional.ofNullable(roles);
	}

	public User(String id, String username, String email, Optional<Set<String>> roles,
			Optional<Set<String>> organizations, Optional<Set<String>> groups) {
		super();
		this.id = id;
		this.username = username;
		this.email = email;
		this.roles = roles;
		this.organizations = organizations;
		this.groups = groups;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Optional<Set<String>> getRoles() {
		return roles;
	}

	public void setRoles(Optional<Set<String>> roles) {
		this.roles = roles;
	}

	public Optional<Set<String>> getOrganizations() {
		return organizations;
	}

	public void setOrganizations(Optional<Set<String>> organizations) {
		this.organizations = organizations;
	}

	public Optional<Set<String>> getGroups() {
		return groups;
	}

	public void setGroups(Optional<Set<String>> groups) {
		this.groups = groups;
	}		

}
