package it.eng.tools.model;

import java.util.List;

public class CategoryBean {
	
	private String id;
	private String scopeName;
	private String categoryName;
	private List<DashboardBean> dashboardList;
	
	public CategoryBean() {
		super();
	}
	
	public CategoryBean(String id, String scopeName, String categoryName, List<DashboardBean> dashboardList) {
		super();
		this.id = id;
		this.scopeName = scopeName;
		this.categoryName = categoryName;
		this.dashboardList = dashboardList;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getScopeName() {
		return scopeName;
	}
	public void setScopeName(String scopeName) {
		this.scopeName = scopeName;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public List<DashboardBean> getDashboardList() {
		return dashboardList;
	}

	public void setDashboardList(List<DashboardBean> dashboardList) {
		this.dashboardList = dashboardList;
	}
}
