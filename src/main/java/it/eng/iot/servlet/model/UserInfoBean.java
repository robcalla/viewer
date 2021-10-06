package it.eng.iot.servlet.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class UserInfoBean implements Serializable{

	/*{
	*	organizations: [ ],
	*	displayName: "annamaria",
	*	roles: [
	*	{
	*	name: "Provider",
	*	id: "provider"
	*	},
	*	{
	*	name: "Provider2",
	*	id: "provider2"
	*	}
	*	],
	*	app_id: "c6734d0c16384a49b4e39cb900288b3c",
	*	isGravatarEnabled: false,
	*	email: "annamaria.cappa@eng.it",
	*	id: "annamaria"
	*	}
	*/

	/**
	 * 
	 */
	private static final long serialVersionUID = -1265830969931413394L;
	
	private Set<Organization> organizations;
	private String displayName;
	private Set<RoleBean> roles;
	private String app_id;
	private boolean isGravatarEnabled;
	private String email;
	private String id;

	public UserInfoBean() {
		organizations = new HashSet<Organization>();
		displayName = "";
		roles = new HashSet<RoleBean>();
		app_id = "";
		isGravatarEnabled = false;
		email = "";
		id = "";
	}

	public UserInfoBean(Set<Organization> organizations,String displayName, Set<RoleBean> role, String app_id, boolean isGravatarEnabled, String email, String id ) {
		this.organizations = organizations;
		this.displayName = displayName;
		this.roles = role;
		this.app_id = app_id;
		this.isGravatarEnabled = isGravatarEnabled;
		this.email = email;
		this.id = id;
	}
	
	public Set<Organization> getOrganizations() {
		return organizations;
	}

	public void setOrganizations(Set<Organization> organizations) {
		this.organizations = organizations;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getApp_id() {
		return app_id;
	}

	public void setApp_id(String app_id) {
		this.app_id = app_id;
	}

	public boolean isGravatarEnabled() {
		return isGravatarEnabled;
	}

	public void setGravatarEnabled(boolean isGravatarEnabled) {
		this.isGravatarEnabled = isGravatarEnabled;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Set<RoleBean> getRoles() {
		return roles;
	}

	public void setRoles(Set<RoleBean> role) {
		this.roles = role;
	}

}
