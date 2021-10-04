package it.eng.tools.model;

public class TenantScopeBean {
	
	private String enabler;
	private String organizationId;
	private String roleId;
	private ScopeEntityBean scope;
	
	public TenantScopeBean() {
		super();
	}

	public TenantScopeBean(String enabler, String organizationId, String roleId, ScopeEntityBean scope) {
		super();
		this.enabler = enabler;
		this.organizationId = organizationId;
		this.roleId = roleId;
		this.scope = scope;
	}

	public String getEnabler() {
		return enabler;
	}

	public void setEnabler(String enabler) {
		this.enabler = enabler;
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public ScopeEntityBean getScope() {
		return scope;
	}

	public void setScope(ScopeEntityBean scope) {
		this.scope = scope;
	}
	
	
	
	
	
	
	
	
	

}
