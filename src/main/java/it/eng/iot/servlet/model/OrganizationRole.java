package it.eng.iot.servlet.model;

import java.util.HashSet;
import java.util.Set;

/*
{
"links": {
"self": "http://cityenabler.eng.it:5000/v3/projects/cef31912da3a40668cda0cf89b6fc0a4/users/gambone/roles",
"previous": null,
"next": null
},
"roles": [
  {
"id": "9eb1c7a2e6f341018c2027f04c36f831",
"links": {
"self": "http://cityenabler.eng.it:5000/v3/roles/9eb1c7a2e6f341018c2027f04c36f831"
},
"name": "member"
}
],
}
 */

public class OrganizationRole {

	private Link links;
	private Set<OrganizationInternalRole> roles;
	
	
	public Link getLinks() {
		return links;
	}
	public void setLink(Link links) {
		this.links = links;
	}

	public Set<OrganizationInternalRole> getRoles() {
		return roles;
	}
	public void setRoles(Set<OrganizationInternalRole> roles) {
		this.roles = roles;
	}
	
	
	
	public OrganizationRole(Link links, Set<OrganizationInternalRole> roles) {
		this.links = links;
		this.roles = roles;

	}

	public OrganizationRole(){
		
		this.links = new Link();
		this.roles = new HashSet<>();
		
	}
	
	
}


