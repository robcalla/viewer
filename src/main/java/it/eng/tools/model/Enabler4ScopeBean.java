package it.eng.tools.model;

public class Enabler4ScopeBean {
	
	private String id;
	private ScopeEntityBean refScope;
	private EnablerBean refEnabler;
	
	public Enabler4ScopeBean() {
		super();
	}

	public Enabler4ScopeBean(String id, ScopeEntityBean refScope, EnablerBean refEnabler) {
		super();
		this.id = id;
		this.refScope = refScope;
		this.refEnabler = refEnabler;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ScopeEntityBean getScope() {
		return refScope;
	}

	public void setScope(ScopeEntityBean refScope) {
		this.refScope = refScope;
	}

	public EnablerBean getEnabler() {
		return refEnabler;
	}

	public void setEnabler(EnablerBean refEnabler) {
		this.refEnabler = refEnabler;
	}
	
	
	
	

}
