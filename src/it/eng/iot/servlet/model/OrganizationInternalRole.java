package it.eng.iot.servlet.model;

import java.util.HashSet;
import java.util.Set;

/*

"roles": [
  {
	"id": "9eb1c7a2e6f341018c2027f04c36f831",
	"links": {"self": "http://cityenabler.eng.it:5000/v3/roles/9eb1c7a2e6f341018c2027f04c36f831"},
	"name": "member"
}
]

 */

public class OrganizationInternalRole {

	private String id;
	private Link links;
	private String name;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Link getLinks() {
		return links;
	}

	public void setLinks(Link links) {
		this.links = links;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public OrganizationInternalRole(String id, Link links, String name) {

		this.id = id;
		this.links = links;
		this.name = name;

	}


	public OrganizationInternalRole(){

		this.id = "";
		this.links = new Link();
		this.name = "";

	}


}


