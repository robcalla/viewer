/**
  {
"id": "sevilla_1",
"type": "scope",
"dateModified": {
"type": "DateTime",
"value": "2017-06-09T07:35:20.00Z",
"metadata": {}
},
"metadata": {}
},
"name": {
"type": "Text",
"value": "sevilla_1",
"metadata": {}
},
"opType": {
"type": "Text",
"value": "created",
"metadata": {}
}
}
*/

package it.eng.tools.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import it.eng.iot.servlet.model.MapCenter;

public class ServiceEntityBean extends CBEntity{
	
	@Override
	public String toString() {
		return "ServiceEntityBean [service=" + service + ", subservice=" + subservice + ", entity_type=" + entity_type
				+ ", apikey=" + apikey + ", resource=" + resource + ", name=" + name
				+ ", dateModified=" + dateModified + ", opType=" + opType + ", refScope=" + refScope + ", dashboardid="
				+ dashboardid + "]";
	}

	private EntityAttribute<String> service; 
	private EntityAttribute<String> subservice; 
	private EntityAttribute<String> entity_type; 
	private EntityAttribute<String> apikey;
	private EntityAttribute<String> resource;
	private EntityAttribute<MapCenter> mapcenter;
	
	private EntityAttribute<String> name; 
	private EntityAttribute<Date> dateModified;
	private EntityAttribute<String> opType;
	private EntityAttribute<String> refScope;
	private EntityAttribute<List<DashboardBean>> dashboardid;
	
	
	public ServiceEntityBean() {
		this(new String("scope"));
	}
	
	public ServiceEntityBean(String type) {
		this(type, 
				new EntityAttribute<String>(new String()),
				new EntityAttribute<String>(new String()), 
				new EntityAttribute<String>(new String()), 
				new EntityAttribute<String>(new String()), 
				new EntityAttribute<String>(new String()),
				new EntityAttribute<MapCenter>(new MapCenter(null, null, null)),
				new EntityAttribute<String>(new String()), 
				new EntityAttribute<Date>(new Date()),
				new EntityAttribute<String>(new String()),
				new EntityAttribute<String>(new String()),
				new EntityAttribute<List<DashboardBean>>(new ArrayList<DashboardBean>())
			);
	}
	
	public ServiceEntityBean(String type,
			//EntityAttribute<String> schema, 
			EntityAttribute<String> service,
			EntityAttribute<String> subservice,
			EntityAttribute<String> entity_type,
			EntityAttribute<String> apikey, 
			EntityAttribute<String> resource,
			EntityAttribute<MapCenter> mapcenter,
			EntityAttribute<String> name, 
			EntityAttribute<Date> dateModified,
			EntityAttribute<String> opType,
			EntityAttribute<String> refScope,
			EntityAttribute<List<DashboardBean>> dashboardid) {
		
		super(new String(), type);
		
		this.entity_type = entity_type;
		this.apikey = apikey;
		this.resource = resource;
		this.mapcenter = mapcenter;
		this.name = name;
		this.dateModified = dateModified;
		this.opType = opType;
		this.refScope = refScope;
		this.dashboardid = dashboardid;
		
		//String _id = schema.getValue().replace("/", "") + "_" + service.getValue().replace("/", "") + "_" + subservice.getValue().replace("/", "");
		String _id = name.getValue();
		this.setId(_id);
		
	}

	
	public EntityAttribute<String> getEntity_type() {
		return entity_type;
	}
	public void setEntity_type(EntityAttribute<String> entity_type) {
		this.entity_type = entity_type;
	}
	
	public EntityAttribute<String> getApikey() {
		return apikey;
	}
	public void setApikey(EntityAttribute<String> apikey) {
		this.apikey = apikey;
	}
	
	public EntityAttribute<String> getResource() {
		return resource;
	}
	public void setResource(EntityAttribute<String> resource) {
		this.resource = resource;
	}	
	
	
	public EntityAttribute<String> getName() {
		return name;
	}
	public void setName(EntityAttribute<String> name) {
		this.name = name;
	}
	
	public EntityAttribute<Date> getDateModified() {
		return dateModified;
	}
	public void setDateModified(EntityAttribute<Date> dateModified) {
		this.dateModified = dateModified;
	}
	
	public EntityAttribute<String> getOpType() {
		return opType;
	}
	public void setOpType(EntityAttribute<String> opType) {
		this.opType = opType;
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

	public void setDashboardid (EntityAttribute<List<DashboardBean>> dashboardid) {
		this.dashboardid = dashboardid;
	}

	public EntityAttribute<MapCenter> getMapcenter() {
		return mapcenter;
	}

	public void setMapcenter(EntityAttribute<MapCenter> mapcenter) {
		this.mapcenter = mapcenter;
	}

	public EntityAttribute<String> getService() {
		return service;
	}

	public void setService(EntityAttribute<String> service) {
		this.service = service;
	}

	public EntityAttribute<String> getSubservice() {
		return subservice;
	}

	public void setSubservice(EntityAttribute<String> subservice) {
		this.subservice = subservice;
	}
	
	
	
	
	
	
	

}
