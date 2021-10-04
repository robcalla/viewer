package it.eng.tools.model;

public class UrbSerEntityBeanOverall extends UrbSerEntityBean {
	
	private Enabler4ScopeBean refEnabler4scope;

	public UrbSerEntityBeanOverall() {
		super();
	}

	public UrbSerEntityBeanOverall(String id, String apikey, String name, String refScope, String refEnabler,
			String servicepath) {
		super(apikey, name, refScope, refEnabler, servicepath);
	}

	public Enabler4ScopeBean getRefEnabler4scope() {
		return refEnabler4scope;
	}

	public void setRefEnabler4scope(Enabler4ScopeBean refEnabler4scope) {
		this.refEnabler4scope = refEnabler4scope;
	}
	
	
	
	

}
