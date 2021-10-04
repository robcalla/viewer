package it.eng.tools.model;


/* 
 *  "urn:scim:schemas:extension:keystone:2.0": {
 *  "domain_id": "default", 
 *  "default_project_id": "idm_project"
 *  }
 * */

public class UrnScimSchema {
	
	private String domain_id;
	private String default_project_id;
	
	
	public String getDomain_id() {
		return domain_id;
	}
	public void setDomain_id(String domain_id) {
		this.domain_id = domain_id;
	}
	
	public String getDefault_project_id() {
		return default_project_id;
	}
	public void setDefault_project_id(String default_project_id) {
		this.default_project_id = default_project_id;
	}
	
	
	public UrnScimSchema(String domain_id, String default_project_id ) {
		this.domain_id = domain_id;
		this.default_project_id = default_project_id;
	}
	
	public UrnScimSchema() {
		this.domain_id = "";
		this.default_project_id = "";
	}
	
}
