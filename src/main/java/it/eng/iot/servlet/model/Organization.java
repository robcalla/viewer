package it.eng.iot.servlet.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/*
 "organizations": [{
		"website": "",
		"description": "Prova",
		"roles": [{
			"name": "Seller",
			"id": "f995c2767c02420ab5cdfacaef3e677c"
		}],
		"enabled": true,
		"id": "9dc40049dd8a41f5a5a76d01b0e669ce",
		"domain_id": "default",
		"name": "Prova"
	}]
 */

public class Organization implements Serializable{


	private static final long serialVersionUID = 5136846001222238747L;
	private String website;
	private String description;
	private Set<RoleBean> roles;
	private boolean enabled;
	private String id;
	private String domain_id;
	private String name;
	
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Set<RoleBean> getRoles() {
		return roles;
	}
	public void setRoles(Set<RoleBean> roles) {
		this.roles = roles;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDomain_id() {
		return domain_id;
	}
	public void setDomain_id(String domain_id) {
		this.domain_id = domain_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	public Organization(String website, String description, Set<RoleBean> roles, boolean enabled, String id, String domain_id, String name) {
	this.website = website;
		this.description = description;
		this.roles = roles;
		this.enabled = enabled;
		this.id = id;
		this.domain_id = domain_id;
		this.name = name;
	}

	public Organization(){
		this.description = "";
		this.roles = new HashSet<RoleBean>();
		this.enabled = true;
		this.id = "";
		this.domain_id = "";
		this.name = "";
	}
	
	
}


