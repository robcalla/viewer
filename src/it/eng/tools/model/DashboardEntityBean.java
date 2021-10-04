package it.eng.tools.model;

import java.util.List;

public class DashboardEntityBean extends CBEntity {
	
	private EntityAttribute<String> name; 
	private EntityAttribute<String> refScope;
	private EntityAttribute<List<DashboardBean>> dashboardid;

	public DashboardEntityBean(String id, String type, EntityAttribute<String> name, EntityAttribute<String> refScope,
			EntityAttribute<List<DashboardBean>> dashboardid) {
		super(id, type);
		this.name = name;
		this.refScope = refScope;
		this.dashboardid = dashboardid;
	}

	public EntityAttribute<String> getName() {
		return name;
	}

	public void setName(EntityAttribute<String> name) {
		this.name = name;
	}

	public EntityAttribute<String> getRefScope() {
		return refScope;
	}

	public void setRefScope(EntityAttribute<String> refScope) {
		this.refScope = refScope;
	}

	public EntityAttribute<List<DashboardBean>> getDashboardid() {
		return dashboardid;
	}

	public void setDashboardid(EntityAttribute<List<DashboardBean>> dashboardid) {
		this.dashboardid = dashboardid;
	}
	
	
	
	

}
