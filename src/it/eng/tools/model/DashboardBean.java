package it.eng.tools.model;

public class DashboardBean {
	
	private String id;
	private String url;
	private String type;
	private String categoryId;
	
	public DashboardBean() {
		super();
	}

	public DashboardBean(String id, String url, String type, String categoryId) {
		super();
		this.id = id;
		this.url = url;
		this.type = type;
		this.categoryId = categoryId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

}
