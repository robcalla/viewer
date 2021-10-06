/*
{
	"id": "polimi_controlroom"
    "apikey": "npPe4bVcb4z0Oyt",
    "name": "Control Room",
    "refScope": "polimi",
    "refEnabler": "city"
    "servicepath": "controlroom"
}
*/

package it.eng.tools.model;

import java.util.List;

public class UrbSerEntityBean {

	private String id;
	private String apikey; 
	private String name; 
	private String refScope;
	private String refEnabler;
	private String servicePath;
	private List<DashboardBean> dashboards;
	
	public UrbSerEntityBean() {
		super();
	}

	public UrbSerEntityBean(
			String apikey, 
			String name,
			String refScope, 
			String refEnabler, 
			String servicePath) {
		this.apikey = apikey;
		this.name = name;
		this.refScope = refScope;
		this.refEnabler = refEnabler;
		this.servicePath = servicePath;
		
	}

	@Override
	public String toString() {
		return "UrbSerEntityBean [id=" + id + ", apikey=" + apikey + ", name=" + name + ", refScope=" + refScope + ", refEnabler="
				+ refEnabler + ", servicePath=" + servicePath + "]";
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getApikey() {
		return apikey;
	}

	public void setApikey(String apikey) {
		this.apikey = apikey;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRefScope() {
		return refScope;
	}

	public void setRefScope(String refScope) {
		this.refScope = refScope;
	}

	public String getRefEnabler() {
		return refEnabler;
	}

	public void setRefEnabler(String refEnabler) {
		this.refEnabler = refEnabler;
	}

	public String getServicePath() {
		return servicePath;
	}

	public void setServicePath(String servicePath) {
		this.servicePath = servicePath;
	}

	public List<DashboardBean> getDashboards() {
		return dashboards;
	}

	public void setDashboards(List<DashboardBean> dashboards) {
		this.dashboards = dashboards;
	}

	
	
	
	
	
	

}
