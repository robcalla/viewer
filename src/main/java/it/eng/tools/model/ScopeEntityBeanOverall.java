package it.eng.tools.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.eng.digitalenabler.fe.permission.ResourcePermission;

public class ScopeEntityBeanOverall extends ScopeEntityBean {
	
	private List<DashboardBean> dashboardid;
	private Map<String,List<DashboardBean>> urbanServices;
	private ResourcePermission permission;
	
	public ScopeEntityBeanOverall(
			String id, 
			String latitude,
			String longitude,
			String zoom,
			String name, 
			String service, 
			String refEnabler,
			String imageUrl) {
		super(id, latitude, longitude, zoom, name, service, refEnabler, imageUrl);
	}
	
	public ScopeEntityBeanOverall(
			String id,
			String latitude,
			String longitude,
			String zoom,
			String name,
			String service,
			String refEnabler,
			String imageUrl,
			List<DashboardBean> dashboardid,
			Map<String,List<DashboardBean>> urbanServices,
			ResourcePermission permission) {
		super(id, latitude, longitude, zoom, name, service, refEnabler, imageUrl);
		this.dashboardid = dashboardid;
		this.urbanServices = urbanServices;
		this.permission = permission;
	}
	
	public ScopeEntityBeanOverall(ScopeEntityBean s) {
		this(s.getId(),
			 s.getLatitude(),
			 s.getLongitude(),
			 s.getZoom(),
			 s.getName(),
			 s.getService(),
			 s.getRefEnabler(),
			 s.getImageUrl());
	}

	public List<DashboardBean> getDashboardid() {
		return dashboardid;
	}

	public void setDashboardid(ArrayList<DashboardBean> dashoboardIds) {
		this.dashboardid = dashoboardIds;
	}

	public Map<String, List<DashboardBean>> getUrbanServices() {
		return urbanServices;
	}

	public void setUrbanServices(Map<String, List<DashboardBean>> uService) {
		this.urbanServices = uService;
	}

	public ResourcePermission getPermission() {
		return permission;
	}

	public void setPermission(ResourcePermission permission) {
		this.permission = permission;
	}
	
	

}
