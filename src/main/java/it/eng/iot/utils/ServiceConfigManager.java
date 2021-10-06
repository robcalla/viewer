package it.eng.iot.utils;

import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.ws.rs.core.MediaType;

import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.jersey.api.client.ClientResponse;

import it.eng.digitalenabler.fe.enumeration.ResourceEnum;
import it.eng.digitalenabler.fe.permission.PermissionManager;
import it.eng.digitalenabler.fe.permission.Resource;
import it.eng.digitalenabler.fe.permission.ResourcePermission;
import it.eng.digitalenabler.fiware.keyrock7.dto.UserDTO;
import it.eng.digitalenabler.identity.manager.model.Organization;
import it.eng.digitalenabler.identity.manager.model.Role;
import it.eng.digitalenabler.keycloak.KeycloakContext;
import it.eng.digitalenabler.keycloak.User;
import it.eng.iot.configuration.Conf;
import it.eng.iot.configuration.ConfIDM;
import it.eng.iot.configuration.ConfOrionCB;
import it.eng.iot.configuration.ConfRabbitMQ;
import it.eng.iot.servlet.model.MapCenter;
import it.eng.tools.LogFilter;
import it.eng.tools.Orion;
import it.eng.tools.model.ActionEntityBean;
import it.eng.tools.model.CategoryBean;
import it.eng.tools.model.DashboardBean;
import it.eng.tools.model.DashboardEntityBean;
import it.eng.tools.model.EntityAttribute;
import it.eng.tools.model.ServiceEntityBean;
import it.eng.tools.model.ServiceEntityBeanOverall;
import it.eng.tools.model.TenantScopeBean;
import it.eng.tools.model.ScopeEntityBeanOverall;
import it.eng.tools.model.UrbSerEntityBean;
import it.eng.tools.model.UrbSerEntityBeanOverall;
import it.eng.tools.model.OrganizationConfigEntity;
import it.eng.tools.model.ScopeEntityBean;

public abstract class ServiceConfigManager {

	private static final Logger LOGGER = Logger.getLogger(ServiceConfigManager.class.getName());
	private static Orion orion;
	private static String tenantUrl = Conf.getInstance().getString("tenant.url");
	private static Boolean isTenantActive = Boolean.valueOf(Conf.getInstance().getString("tenant.enabled"));
	private static String persistenceUrl = Conf.getInstance().getString("persistence.url");

	static {
		try {
			orion = new Orion();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	static {
		LogFilter logFilter = new LogFilter();
		LOGGER.setFilter(logFilter);
	}

	public static String createServiceConfiguration(ServiceEntityBean serviceConfig) throws Exception {

		HashSet<ServiceEntityBean> serviceConfigs = new HashSet<ServiceEntityBean>();
		serviceConfigs.add(serviceConfig);

		JSONObject out = orion.postEntities(ConfOrionCB.getInstance().getString("orion.headers.service"),
				ConfOrionCB.getInstance().getString("orion.headers.servicepath"), serviceConfigs);
		LOGGER.log(Level.INFO, "Created service: " + out.toString());
		return out.toString();
	}

	public static Set<ServiceEntityBean> getServices(String scope) {

		String headerService = ConfOrionCB.getInstance().getString("orion.headers.service");
		String headerServicePath = ConfOrionCB.getInstance().getString("orion.headers.servicepath");

		Set<BasicNameValuePair> params = new HashSet<BasicNameValuePair>();
		params.add(new BasicNameValuePair("type", "scope"));
		if (scope != null)
			params.add(new BasicNameValuePair("q", "refScope=='" + scope + "'"));

		String resp = orion.getEntities(headerService, headerServicePath, params);

		Type type = new TypeToken<Set<ServiceEntityBean>>() {
		}.getType();
		Set<ServiceEntityBean> apiResp = new Gson().fromJson(resp, type);

		return apiResp;
	}
	//Get contexts from TenantManager
	public static Set<ScopeEntityBean> getTMContexts(String enabler) {

		String resp = "";
		Map<String,String> headers = new HashMap<String,String>();
		KeycloakContext kc = new KeycloakContext();
		headers.put("Authorization", "Bearer " + kc.getServiceAccessToken());
		try {
			if(enabler != null) {
				resp = RestUtils.consumeGet(tenantUrl + "?enabler=" + enabler, headers);
			} else {
				resp = RestUtils.consumeGet(tenantUrl, headers);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Type type = new TypeToken<Set<TenantScopeBean>>() {
		}.getType();

		Set<TenantScopeBean> tenResp = new Gson().fromJson(resp, type);
		Set<ScopeEntityBean> result = new HashSet<ScopeEntityBean>();

		if(tenResp != null) {
			for (TenantScopeBean tenant:tenResp) {
				ScopeEntityBean newBean = new ScopeEntityBean();
				newBean = tenant.getScope();
				newBean.setRefEnabler(tenant.getEnabler());
				result.add(newBean);
			}
		}

		return result;
	}
	//Get categories from TenantManager
	public static Set<UrbSerEntityBean> getTMCategories(String contextId) {

		String resp = "";
		String orionResp = "";
		String headerService = ConfOrionCB.getInstance().getString("orion.headers.service");
		String headerServicePath = ConfOrionCB.getInstance().getString("orion.headers.servicepath");

		try {
			Map<String, String> headers = new HashMap<String, String>();
			KeycloakContext kc = new KeycloakContext();
			headers.put("Authorization", "Bearer " + kc.getServiceAccessToken());
			resp = RestUtils.consumeGet(tenantUrl + "/" + contextId + "/verticals", headers);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Set<BasicNameValuePair> params = new HashSet<BasicNameValuePair>();
		params.add(new BasicNameValuePair("type", "dashboard_info"));
		if(!isTenantActive) {
			orionResp = orion.getEntities(headerService, headerServicePath, params);
		}

		Type type = new TypeToken<Set<UrbSerEntityBeanOverall>>() {
		}.getType();

		Set<UrbSerEntityBeanOverall> tenResp = new Gson().fromJson(resp, type);
		Set<UrbSerEntityBean> result = new HashSet<UrbSerEntityBean>();

		for(UrbSerEntityBeanOverall tenant:tenResp) {
			UrbSerEntityBean newBean = new UrbSerEntityBean();
			newBean.setId(tenant.getId());
			newBean.setApikey(tenant.getApikey());
			newBean.setName(tenant.getName());
			newBean.setRefEnabler(tenant.getRefEnabler4scope().getEnabler().getName());
			newBean.setRefScope(tenant.getRefEnabler4scope().getScope().getService());
			newBean.setServicePath(tenant.getServicePath());

			List<DashboardBean> dashboards = getDashboardsFromPersistence(tenant.getId());
			newBean.setDashboards(dashboards);

			result.add(newBean);
		}

		return result;
	}

	public static boolean createCategory(String context, String category, String dashboardName, String type, String enabler) {
		String headerService = ConfOrionCB.getInstance().getString("orion.headers.service");
		String headerServicePath = ConfOrionCB.getInstance().getString("orion.headers.servicepath");
		String entityString = "";
		String serviceId = category.toLowerCase().replaceAll("\\s", "_");
		Date date = new Date();
		List<DashboardBean> dashboardIdList = new ArrayList<DashboardBean>();
		//UUID uuid = UUID.randomUUID();
		//String idValue = uuid.toString();

		if (dashboardName != null) {
			DashboardBean dashboard = new DashboardBean();
			dashboard.setId(category + "_" + dashboardName);
			dashboard.setUrl(dashboardName);
			dashboard.setType(type);
		}

		ServiceEntityBean serviceEntity = new ServiceEntityBean("urbanservice", null, // service
				null, // subservice
				null, // entity_type
				new EntityAttribute<String>(CommonUtils.randomAlphanumericString(15)), // api_key
				null, // resource
				null, // map_center
				new EntityAttribute<String>(category), // name
				new EntityAttribute<Date>(date), // dateModified
				new EntityAttribute<String>("created"), // opType
				new EntityAttribute<String>(context), // refScope
				new EntityAttribute<List<DashboardBean>>(dashboardIdList) // DashBoardId
		);

		serviceEntity.setId(context + "_" + serviceId);
		entityString = new Gson().toJson(serviceEntity);
		JSONObject entity = new JSONObject(entityString);

		boolean result = false;

		if(isTenantActive) {
			UrbSerEntityBean urbserEntity = new UrbSerEntityBean(CommonUtils.randomAlphanumericString(15), // api_key
					category, // name
					context, // refScope
					enabler, // refEnabler
					serviceId // servicePath
			);

			CategoryBean dashboardBean = new CategoryBean(
					context + "_" + serviceId, //id
					context, //scopeName
					category, //categoryName
					dashboardIdList //dashboardList
			);

			urbserEntity.setId(context + "_" + serviceId);
			String urbserString = new Gson().toJson(urbserEntity);
			ActionEntityBean actionEntity = new ActionEntityBean("CREATE", urbserString);
			JSONObject rabbitmqUrbSer = new JSONObject(actionEntity);

			String dashboardString = new Gson().toJson(dashboardBean);
			JSONObject dashboardObject = new JSONObject(dashboardString);

			// Creates urbanservice in RabbitMQ
			if (ConfRabbitMQ.getInstance().getString("RMQ.enabled").equalsIgnoreCase("true")) {
				RabbitMQManager.sendToQueue(ConfRabbitMQ.getInstance().getString("RMQUrbanService"), rabbitmqUrbSer);
				if(dashboardName != null) {
					try {
						RestUtils.consumePost(persistenceUrl, dashboardObject, null);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

		}

		if(!isTenantActive) {
			result = orion.postEntity(headerService, headerServicePath, entity);
		}

		return result;
	}

	/* Return the scope name */
	public static String getContextName(String id) {
		String contextName = "";

		if(isTenantActive) {
			contextName = getTMContextName(id);
		} else {
			String headerService = ConfOrionCB.getInstance().getString("orion.headers.service");
			String headerServicePath = ConfOrionCB.getInstance().getString("orion.headers.servicepath");

			Set<BasicNameValuePair> params = new HashSet<BasicNameValuePair>();
			params.add(new BasicNameValuePair("type", "scope"));
			params.add(new BasicNameValuePair("id", id));

			String resp = orion.getEntities(headerService, headerServicePath, params);
			Type type = new TypeToken<Set<ServiceEntityBean>>() {
			}.getType();
			Set<ServiceEntityBean> apiResp = new Gson().fromJson(resp, type);

			for (ServiceEntityBean serv : apiResp) {
				contextName = serv.getName().getValue();
			}
		}

		LOGGER.log(Level.INFO, "Scope: " + id + " - " + contextName);
		return contextName;
	}
	//Get context name from TenantManager
	public static String getTMContextName(String id) {

		String resp = "";
		try {
			Map<String,String> headers = new HashMap<String,String>();
			KeycloakContext kc = new KeycloakContext();
			headers.put("Authorization", "Bearer " + kc.getServiceAccessToken());
			resp = RestUtils.consumeGet(tenantUrl + "/" + id, headers);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Type type = new TypeToken<TenantScopeBean>() {
		}.getType();

		TenantScopeBean tenResp = new Gson().fromJson(resp, type);
		resp = tenResp.getScope().getName();

		return resp;
	}

	public static Set<ServiceEntityBean> getCategoriesByContextName(String contextName) {

		String headerService = ConfOrionCB.getInstance().getString("orion.headers.service");
		String headerServicePath = ConfOrionCB.getInstance().getString("orion.headers.servicepath");

		Set<BasicNameValuePair> params = new HashSet<BasicNameValuePair>();
		params.add(new BasicNameValuePair("type", "urbanservice"));
		params.add(new BasicNameValuePair("q", "refScope=='" + contextName + "'"));

		String resp = orion.getEntities(headerService, headerServicePath, params);

		Type type = new TypeToken<Set<ServiceEntityBean>>() {
		}.getType();
		Set<ServiceEntityBean> apiResp = new Gson().fromJson(resp, type);

		return apiResp;
	}

	/* Return the urbanservice name */
	public static String getCategoryName(String categoryId, String refScope) {
		String serviceName = "";

		if(isTenantActive) {
			serviceName = getTMCategoryName(refScope, categoryId);
		} else {
			String headerService = ConfOrionCB.getInstance().getString("orion.headers.service");
			String headerServicePath = ConfOrionCB.getInstance().getString("orion.headers.servicepath");

			Set<BasicNameValuePair> params = new HashSet<BasicNameValuePair>();
			params.add(new BasicNameValuePair("type", "urbanservice"));
			params.add(new BasicNameValuePair("id", categoryId.replace("/", "")));
			// params.add(new BasicNameValuePair("refScope", refScope));
			params.add(new BasicNameValuePair("q", "refScope=='" + refScope + "'"));
			// TODO: CORREGGERE!!! q=refScope==refScope

			String resp = orion.getEntities(headerService, headerServicePath, params);
			Type type = new TypeToken<Set<ServiceEntityBean>>() {
			}.getType();
			Set<ServiceEntityBean> apiResp = new Gson().fromJson(resp, type);

			for (ServiceEntityBean serv : apiResp) {
				serviceName = serv.getName().getValue();
			}
		}

		LOGGER.log(Level.INFO, "Urbanservice: " + categoryId + " - " + serviceName);
		return serviceName;
	}
	//Get category name from TenantManager
	public static String getTMCategoryName(String id, String categoryId) {

		String resp = "";
		try {
			Map<String,String> headers = new HashMap<String,String>();
			KeycloakContext kc = new KeycloakContext();
			headers.put("Authorization", "Bearer " + kc.getServiceAccessToken());
			resp = RestUtils.consumeGet(tenantUrl + "/" + id + "/verticals/" + categoryId, headers);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Type type = new TypeToken<UrbSerEntityBean>() {
		}.getType();

		UrbSerEntityBean tenResp = new Gson().fromJson(resp, type);
		resp = tenResp.getName();

		return resp;

	}

	/*
	 * Get apikey retrieves value from cedus configuration entities
	 */
	public static String getApikey(String serviceId, String servicepathId) {

		String headerService = ConfOrionCB.getInstance().getString("orion.headers.service");
		String headerServicePath = ConfOrionCB.getInstance().getString("orion.headers.servicepath");
		String apikey = "";

		Set<BasicNameValuePair> params = new HashSet<BasicNameValuePair>();
		params.add(new BasicNameValuePair("type", "urbanservice"));
		params.add(new BasicNameValuePair("id", servicepathId.replace("/", "")));
		params.add(new BasicNameValuePair("q", "refScope=='" + serviceId + "'"));

		String resp = orion.getEntities(headerService, headerServicePath, params);

		Type type = new TypeToken<Set<ServiceEntityBean>>() {
		}.getType();
		Set<ServiceEntityBean> apiResp = new Gson().fromJson(resp, type);

		for (ServiceEntityBean serv : apiResp) {
			apikey = serv.getApikey().getValue();
		}
		LOGGER.log(Level.INFO, "apikey: " + apikey);

		return apikey;
	}

	/*
	 * Get apikey retrieves value from cedus configuration organization entities
	 */
	public static String getApikey(String organizationId) {

		String headerService = ConfOrionCB.getInstance().getString("orion.headers.service");
		String headerServicePath = ConfOrionCB.getInstance().getString("orion.headers.servicepath");
		String apikey = "";

		String resp = orion.getEntity(headerService, headerServicePath, organizationId);

		Type type = new TypeToken<OrganizationConfigEntity>() {
		}.getType();
		OrganizationConfigEntity org = new Gson().fromJson(resp, type);
		apikey = org.getApikey().getValue();

		return apikey;
	}

	/**
	 * Allows to get the scopes permitted according to the configured roles internal
	 * to the user organization as defined into IDM and containing the configured
	 * string filterscope.convention.string set into the IDM configuration
	 * properties file
	 */
	@Deprecated
	public static Set<ServiceEntityBean> getPermittedServices_V1(Set<ServiceEntityBean> services, Map<Organization, Map<String, List<Role>>> orgs) {

		Set<ServiceEntityBean> serviceSubset = new HashSet<ServiceEntityBean>();
		String scopeConventionString = ConfIDM.getInstance().getString("filterscope.convention.string").trim()
				.toLowerCase();

		Set<Organization> orgList = orgs.keySet();
		if (!scopeConventionString.isEmpty()) {
			for (Organization org : orgList) {
				List<Role> roles = orgs.get(org).get(UserDTO.ORGANIZATION_ROLE);
				for (Role role : roles) {
					String roleName = role.getName();
					if (roleName.toLowerCase().contains(scopeConventionString)) {
						// turn only the specific scope
						for (ServiceEntityBean serv : services) {
							if (serv.getId().equalsIgnoreCase(roleName.replace(scopeConventionString, ""))) {
								serviceSubset.add(serv);
								break;
							}
						}
						// services.retainAll(serviceSubset);
					}

				}
			}
			services.retainAll(serviceSubset);
		}

		LOGGER.log(Level.INFO, "==================================");
		LOGGER.log(Level.INFO, "Number of permitted scopes: " + services.size());
		return services;
	}


	public static Map<ServiceEntityBean,ResourcePermission> getPermittedServices(Set<ServiceEntityBean> services, User userInfo) {

		Map<ServiceEntityBean,ResourcePermission> serviceSubset = new HashMap<ServiceEntityBean,ResourcePermission>();
		PermissionManager pManager = new PermissionManager();
		for(ServiceEntityBean service : services) {
			Resource resource = new Resource(ResourceEnum.CONTEXT,service.getId());
			ResourcePermission servicePerm = pManager.getContextPermission(userInfo, resource);
			if(servicePerm.getCanRead()) {
				serviceSubset.put(service,servicePerm);
			}
		}

		LOGGER.log(Level.INFO, "==================================");
		LOGGER.log(Level.INFO, "Number of permitted scopes: " + serviceSubset.size());
		return serviceSubset;
	}

	public static Map<ScopeEntityBean,ResourcePermission> getPermittedScopes(Set<ScopeEntityBean> scopes, User userInfo) {

		Map<ScopeEntityBean,ResourcePermission> scopeSubset = new HashMap<ScopeEntityBean,ResourcePermission>();
		Set<String> groups = userInfo.getGroups().get();
		Set<String> organizations = userInfo.getOrganizations().get()
			.stream()
	        .map(String::toLowerCase)
	        .collect(Collectors.toSet());

		for(ScopeEntityBean scope : scopes) {
			String permission = "";
			if(groups.contains(scope.getId())) {
				permission += "R";
			}
			if(organizations.contains(scope.getId()) || userInfo.getRoles().get().contains("developer")) {
				permission += "CU";
			}
			if(userInfo.getRoles().get().contains("admin")) {
				permission += "D";
			}

			ResourcePermission scopePerm = new ResourcePermission(ResourceEnum.CONTEXT, permission);
			if(scopePerm.getCanRead()) {
				scopeSubset.put(scope,scopePerm);
			}
		}

		LOGGER.log(Level.INFO, "==================================");
		LOGGER.log(Level.INFO, "Number of permitted scopes: " + scopeSubset.size());
		return scopeSubset;
	}

	public static Set<String> getCockpitName(String urbanService) {
		String headerService = ConfOrionCB.getInstance().getString("orion.headers.service");
		String headerServicePath = ConfOrionCB.getInstance().getString("orion.headers.servicepath");
		String result = "";

		if(isTenantActive) {
			urbanService+="?type=dashboard_info";
			result = orion.getEntity(headerService, headerServicePath, urbanService);
		} else {
			result = orion.getEntity(headerService, headerServicePath, urbanService);
		}

		JSONObject entity = new JSONObject(result);

		JSONArray dashboards = entity.getJSONObject("dashboardid").getJSONArray("value");
		Set<String> dashboardSet = new HashSet<String>();

		for (int i = 0; i < dashboards.length(); i++) {
			dashboardSet.add(dashboards.getString(i));
		}

		return dashboardSet;

	}

	// Never called possibly
	public static Boolean setCockpitName(String entityId, String cockpitName, String cockpitUrl, String type) {
		String headerService = ConfOrionCB.getInstance().getString("orion.headers.service");
		String headerServicePath = ConfOrionCB.getInstance().getString("orion.headers.servicepath");
		System.out.println("Entity ID: " + entityId + " cockpitName: " + cockpitName);
		Set<String> cockpitSet = new HashSet<String>();
		cockpitSet.add(cockpitName);
		Boolean result = orion.updateEntityAttribute(headerService, headerServicePath, entityId, "dashboardid",
				cockpitSet);
		System.out.println("Result from Orion: " + result);
		return result;
	}

	public static Boolean appendCockpitName(String entityId, String cockpitName) {

		String headerService = ConfOrionCB.getInstance().getString("orion.headers.service");
		String headerServicePath = ConfOrionCB.getInstance().getString("orion.headers.servicepath");

		String result;
		if(isTenantActive) {
			result = orion.getEntity(headerService, headerServicePath, entityId.concat("?type=dashboard_info"));
		} else {
			result = orion.getEntity(headerService, headerServicePath, entityId);
		}


		JSONObject entity = new JSONObject(result);

		JSONArray dashboards = entity.getJSONObject("dashboardid").getJSONArray("value");
		List<String> dashboardSet = new ArrayList<String>();

		for (int i = 0; i < dashboards.length(); i++) {
			dashboardSet.add(dashboards.getString(i));
		}

		if (!dashboardSet.contains(cockpitName))
			dashboardSet.add(cockpitName);

		Boolean updateResult;

		if(isTenantActive) {
			updateResult = orion.updateEntityAttributeWithType(headerService, headerServicePath, entityId, "dashboard_info", "dashboardid",
				dashboardSet);
		} else {
			updateResult = orion.updateEntityAttributeWithType(headerService, headerServicePath, entityId, "urbanservice", "dashboardid",
				dashboardSet);
		}

		System.out.println("Result from Orion: " + result);
		return updateResult;
	}
	// Add a dashboard to existing category
	public static boolean appendCockpitFromPersistence(String categoryId, String cockpitName, String cockpitUrl, String type) {
		List<DashboardBean> cockpits = new ArrayList<>();

		CategoryBean category = new CategoryBean(
			categoryId,
			categoryId.substring(0, categoryId.indexOf("_")),
			categoryId.substring(categoryId.indexOf("_") + 1),
			cockpits
		);

		DashboardBean dashboard = new DashboardBean(
				cockpitName,
				cockpitUrl,
				type,
				categoryId
		);

		cockpits.add(dashboard);

		category.setDashboardList(cockpits);

		String categoryString = new Gson().toJson(category);
		JSONObject categoryObject = new JSONObject(categoryString);

		try {
			RestUtils.consumePost(persistenceUrl, categoryObject, null);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static Boolean removeDashboardByName(String entityId, String cockpitName) {

		String headerService = ConfOrionCB.getInstance().getString("orion.headers.service");
		String headerServicePath = ConfOrionCB.getInstance().getString("orion.headers.servicepath");
		String result;
		if(isTenantActive) {
			result = orion.getEntity(headerService, headerServicePath, entityId.concat("?type=dashboard_info"));
		} else {
			result = orion.getEntity(headerService, headerServicePath, entityId);
		}

		JSONObject entity = new JSONObject(result);

		JSONArray dashboards = entity.getJSONObject("dashboardid").getJSONArray("value");
		Set<String> dashboardSet = new HashSet<String>();

		for (int i = 0; i < dashboards.length(); i++) {
			dashboardSet.add(dashboards.getString(i));
		}

		if (dashboardSet.contains(cockpitName))
			dashboardSet.remove(cockpitName);
		Boolean updateResult;

		if(isTenantActive) {
			updateResult = orion.updateEntityAttributeWithType(headerService, headerServicePath, entityId, "dashboard_info", "dashboardid",
				dashboardSet);
		} else {
			updateResult = orion.updateEntityAttributeWithType(headerService, headerServicePath, entityId, "urbanservice", "dashboardid",
				dashboardSet);
		}

		System.out.println("Result from Orion: " + result);
		return updateResult;
	}
	// Remove a dashboard from existing category
	public static Boolean removeDashboardFromPersistence(String entityId, String cockpitName) {

		try {
			String encodedName = URLEncoder.encode(cockpitName, StandardCharsets.UTF_8.toString());
			RestUtils.consumePut(persistenceUrl + "/" + entityId + "/" + encodedName, new Object(), MediaType.APPLICATION_JSON_TYPE, null);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static Boolean deleteAllDashboards(String entityId, String category) {
		String headerService = ConfOrionCB.getInstance().getString("orion.headers.service");
		String headerServicePath = ConfOrionCB.getInstance().getString("orion.headers.servicepath");
		Boolean result = false;

		ActionEntityBean actionEntity = new ActionEntityBean("DELETE", entityId);
		// Delete an urbanservice via RabbitMQ
		if(isTenantActive) {
			if(ConfRabbitMQ.getInstance().getString("RMQ.enabled").equalsIgnoreCase("true")){
				RabbitMQManager.sendToQueue(ConfRabbitMQ.getInstance().getString("RMQUrbanService"), actionEntity);
				try {
					entityId = entityId.concat("?type=dashboard_info");
					result = orion.deleteEntity(headerService, headerServicePath, entityId);
				} catch (Exception e) {
					result = false;
					e.printStackTrace();
				}
			}
		} else {
			try {
				entityId = entityId.concat("?type=urbanservice");
				result = orion.deleteEntity(headerService, headerServicePath, entityId);
			} catch (Exception e) {
				result = false;
				e.printStackTrace();
			}
		}

		return result;
	}
	//Delete all dashboards in a category
	public static Boolean deleteAllPersistedDashboards(String entityId) {
		ClientResponse resp;
		ActionEntityBean actionEntity = new ActionEntityBean("DELETE", entityId);
		if(ConfRabbitMQ.getInstance().getString("RMQ.enabled").equalsIgnoreCase("true")){
			RabbitMQManager.sendToQueue(ConfRabbitMQ.getInstance().getString("RMQUrbanService"), actionEntity);
		}
		try {
			resp = RestUtils.consumeDelete(persistenceUrl + "/" + entityId, null);
			if(resp.getStatus() == 200) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean createScope(String scopeName, String enabler) {
		String headerService = ConfOrionCB.getInstance().getString("orion.headers.service");
		String headerServicePath = ConfOrionCB.getInstance().getString("orion.headers.servicepath");
		String scopeId = scopeName.trim().toLowerCase().replaceAll("\\s", "_");
		boolean result = false;
		MapCenter mapcenter = null;
		EntityAttribute<MapCenter> mapCenterEntity = null;

		Set<BasicNameValuePair> queryParams = new HashSet<BasicNameValuePair>();
		queryParams.add(new BasicNameValuePair("id", scopeId));

		try {
			mapcenter = getOSMMapLocation(scopeName);
			mapCenterEntity = new EntityAttribute<MapCenter>(mapcenter);
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "Impossible to find map center of " + scopeName);
			LOGGER.log(Level.WARNING, e.getMessage());
		}

		if(!isTenantActive) {
			String nameCheck = orion.getEntities(headerService, headerServicePath, queryParams);
			if(nameCheck.length() > 3) {
				return false;
			}
			Date date = new Date();

			ServiceEntityBean serviceEntity = new ServiceEntityBean("scope", null, // service
					null, // subservice
					null, // entity_type
					null, // api_key
					null, // resource
					mapCenterEntity, // map_center
					new EntityAttribute<String>(scopeName), // name
					new EntityAttribute<Date>(date), // dateModified
					new EntityAttribute<String>("created"), // opType
					new EntityAttribute<String>(enabler), // refScope
					null // DashBoardId
			);

			serviceEntity.setId(scopeId);
			String entityString = new Gson().toJson(serviceEntity);
			JSONObject entity = new JSONObject(entityString);

			result = orion.postEntity(headerService, headerServicePath, entity);

		} else {
			//UUID uuid = UUID.randomUUID();
			//String idValue = uuid.toString();
			ScopeEntityBean scopeEntity = new ScopeEntityBean(
					scopeId, //id
					mapcenter.getLat().toString(), //latitude
					mapcenter.getLng().toString(), //longitude
					mapcenter.getZoom().toString(), //zoom
					scopeName, //name
					scopeId, //service
					enabler, //enabler
					"" //image url
			);

			scopeEntity.setImageUrl(scopeId + ".jpg");
			String scopeString = new Gson().toJson(scopeEntity);
			ActionEntityBean actionEntity = new ActionEntityBean("CREATE", scopeString);
			JSONObject rabbitmqScope = new JSONObject(actionEntity);

			// Create scope via RabbitMQ
			if (ConfRabbitMQ.getInstance().getString("RMQ.enabled").equalsIgnoreCase("true")) {
				if(contextExists(scopeId)) {
					result = false;
				} else {
					RabbitMQManager.sendToQueue(ConfRabbitMQ.getInstance().getString("RMQScope"), rabbitmqScope);
					result = true;
				}
			}
		}

		return result;
	}

	public static MapCenter getOSMMapLocation(String keyword) throws Exception {

		String endpoint = Conf.getInstance().getString("osm.api");

		Set<BasicNameValuePair> queryParams = new HashSet<BasicNameValuePair>();
		queryParams.add(new BasicNameValuePair("q", keyword));
		queryParams.add(new BasicNameValuePair("format", "json"));
		queryParams.add(new BasicNameValuePair("polygon", "0"));
		queryParams.add(new BasicNameValuePair("addressdetails", "0"));

		String queryString = "?" + URLEncodedUtils.format(queryParams, Charset.defaultCharset());
		String url = endpoint + queryString;
		String r;
		try {
			r = RestUtils.consumeGet(url);
		} catch (Exception e) {
			// Set random coordinates if GET request fails (for testing)
			MapCenter mapcenter = new MapCenter();
			mapcenter = getRandomMapCenter();
			mapcenter.setZoom(15);
			return mapcenter;
		}

		JSONArray jsArray = new JSONArray(r);
		MapCenter mapcenter = new MapCenter();

		for (int i = 0; i < jsArray.length(); i++) {
			try {
				JSONObject record = jsArray.getJSONObject(i);
				if (record.has("type")) {
					String resultType = record.getString("type");
					if ("city".equals(resultType)) {
						if (record.has("lat") && record.has("lon")) {
							mapcenter.setLat(record.getDouble("lat"));
							mapcenter.setLng(record.getDouble("lon"));
							if (record.has("zoom")) {
								mapcenter.setZoom(record.getInt("zoom"));
							}
							break;
						}
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		return mapcenter;
	}

	public static Boolean deleteCategoriesByContextName(String context) {

		context = context.trim().toLowerCase();
		String headerService = ConfOrionCB.getInstance().getString("orion.headers.service");
		String headerServicePath = ConfOrionCB.getInstance().getString("orion.headers.servicepath");
		Set<BasicNameValuePair> queryParams = new HashSet<BasicNameValuePair>();
		Boolean returnResult = false;
		String entityId = "";

		if(!isTenantActive) {
			queryParams.add(new BasicNameValuePair("type", "urbanservice"));
			queryParams.add(new BasicNameValuePair("q", "refScope==" + context));
		} else {
			queryParams.add(new BasicNameValuePair("type", "dashboard_info"));
			queryParams.add(new BasicNameValuePair("q", "refScope==" + context));
		}
			// Get all the dashboard from scope name
			String result = orion.getEntities(headerService, headerServicePath, queryParams);
			JSONArray categories = new JSONArray(result);
			returnResult = true;
			try {
				for (int i = 0; i < categories.length(); i++) {
					JSONObject category = categories.getJSONObject(i);
					entityId = category.getString("id");

					RestUtils.consumeDelete(persistenceUrl + "/" + entityId, null);

				}
			} catch (Exception e) {
				returnResult = false;
				e.printStackTrace();
			}

		return returnResult;
	}

	public static Boolean deleteContext(String context) {
		context = context.trim().toLowerCase();
		String headerService = ConfOrionCB.getInstance().getString("orion.headers.service");
		String headerServicePath = ConfOrionCB.getInstance().getString("orion.headers.servicepath");
		Boolean returnResult = false;

		ActionEntityBean actionEntity = new ActionEntityBean("DELETE", context);

		//Delete a context via RabbitMQ
		if(ConfRabbitMQ.getInstance().getString("RMQ.enabled").equalsIgnoreCase("true")){
			RabbitMQManager.sendToQueue(ConfRabbitMQ.getInstance().getString("RMQScope"), actionEntity);
			returnResult = true;
		}

		try {
			RestUtils.consumeDelete(persistenceUrl + "/context/" + context, null);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if(!isTenantActive) {
			try {
				returnResult = orion.deleteEntity(headerService, headerServicePath, context);
			} catch (Exception e) {
				returnResult = false;
				e.printStackTrace();
			}
		}
		return returnResult;
	}

	public static List<ServiceEntityBeanOverall> addServiceInfo(
			Map<ServiceEntityBean, ResourcePermission> permittedServices) {

		List<ServiceEntityBeanOverall> result = new ArrayList<ServiceEntityBeanOverall>();
		Set<ServiceEntityBean> services = permittedServices.keySet();
		// For Each Scope
		for (ServiceEntityBean s : services) {
			ServiceEntityBeanOverall o = new ServiceEntityBeanOverall(s);

			ArrayList<DashboardBean> dashoboardIds = new ArrayList<DashboardBean>();
			Set<ServiceEntityBean> servicepaths = ServiceConfigManager.getCategoriesByContextName(s.getId());
			Map<String, List<DashboardBean>> uService = new TreeMap<String, List<DashboardBean>>();
			// For every urbanservices in scope
			for (ServiceEntityBean sP : servicepaths) {

				String uServiceName = sP.getName().getValue();
				List<DashboardBean> uServiceDashboards = new ArrayList<DashboardBean>();

				uServiceDashboards.addAll(sP.getDashboardid().getValue());
				dashoboardIds.addAll(uServiceDashboards);

				uService.put(uServiceName, uServiceDashboards);
			}

			o.setUrbanServices(new EntityAttribute<Map<String, List<DashboardBean>>>(uService));
			o.setDashboardid(new EntityAttribute<List<DashboardBean>>(dashoboardIds));
			o.setPermission(new EntityAttribute<ResourcePermission>(permittedServices.get(s)));
			result.add(o);
		}

		List<ServiceEntityBeanOverall> sortedResult = result.stream().sorted((e1, e2) -> e1.getName().getValue().toLowerCase().compareTo(e2.getName().getValue().toLowerCase())).collect(Collectors.toList());

		return sortedResult;
	}

	public static List<ScopeEntityBeanOverall> addScopeInfo(Map<ScopeEntityBean, ResourcePermission> permittedScopes, Set<DashboardEntityBean> dashboards) {

		List<ScopeEntityBeanOverall> result = new ArrayList<ScopeEntityBeanOverall>();
		Set<ScopeEntityBean> scopes = permittedScopes.keySet();

		// For Each Scope
		for (ScopeEntityBean s : scopes) {
			ScopeEntityBeanOverall o = new ScopeEntityBeanOverall(s);

			ArrayList<DashboardBean> dashoboardIds = new ArrayList<DashboardBean>();
			Map<String, List<DashboardBean>> uService = new TreeMap<String, List<DashboardBean>>();
			Set<UrbSerEntityBean> tenantCategories = new HashSet<UrbSerEntityBean>();
			tenantCategories = ServiceConfigManager.getTMCategories(s.getId());

			// For every urbanservices in scope
			for (UrbSerEntityBean sP : tenantCategories) {

				String uServiceName = sP.getName();
				List<DashboardBean> uServiceDashboards = new ArrayList<DashboardBean>();
				List<DashboardBean> scopeDashboards = new ArrayList<DashboardBean>();
				if(dashboards != null) {
					for(DashboardEntityBean d:dashboards) {
						if(sP.getId().equalsIgnoreCase(d.getId())) {
							scopeDashboards = d.getDashboardid().getValue();
						}

					}
				} else {
					scopeDashboards = sP.getDashboards();
				}

				uServiceDashboards.addAll(scopeDashboards);
				dashoboardIds.addAll(uServiceDashboards);

				uService.put(uServiceName, uServiceDashboards);
			}

			o.setUrbanServices(uService);
			o.setDashboardid(dashoboardIds);
			o.setPermission(permittedScopes.get(s));
			result.add(o);
		}

		List<ScopeEntityBeanOverall> sortedResult = result.stream().sorted((e1, e2) -> e1.getName().compareTo(e2.getName())).collect(Collectors.toList());

		return sortedResult;
	}

	public static List<ServiceEntityBeanOverall> addServiceInfo(Set<ServiceEntityBean> services,
			ResourcePermission sellerPermission) {

		Map<ServiceEntityBean, ResourcePermission> permittedServices = new HashMap<ServiceEntityBean, ResourcePermission>();
		for(ServiceEntityBean s: services) {
			permittedServices.put(s, sellerPermission);
		}

		return addServiceInfo(permittedServices);
	}

	public static List<ScopeEntityBeanOverall> addScopeInfo(Set<ScopeEntityBean> scopes, ResourcePermission sellerPermission) {

		Map<ScopeEntityBean, ResourcePermission> permittedServices = new HashMap<ScopeEntityBean, ResourcePermission>();

		for(ScopeEntityBean s: scopes) {
			permittedServices.put(s, sellerPermission);
		}

		Set<ScopeEntityBean> permScopes = permittedServices.keySet();
		List<ScopeEntityBeanOverall> altResult = new ArrayList<ScopeEntityBeanOverall>();

		for(ScopeEntityBean s: permScopes) {
			Map<String, List<DashboardBean>> urbanServices = new HashMap<String, List<DashboardBean>>();
			Set<UrbSerEntityBean> categories = getTMCategories(s.getId());
			for(UrbSerEntityBean category : categories) {
				List<DashboardBean> dashboards = getDashboardsFromPersistence(category.getId());
				urbanServices.put(category.getName(), dashboards);
			}
			ScopeEntityBeanOverall altBean = new ScopeEntityBeanOverall(s);
			altBean.setPermission(sellerPermission);
			altBean.setUrbanServices(urbanServices);
			altResult.add(altBean);
		}

		List<ScopeEntityBeanOverall> sortedResult = altResult.stream().sorted((e1, e2) -> e1.getName().toLowerCase().compareTo(e2.getName().toLowerCase())).collect(Collectors.toList());

		return sortedResult;
	}
	// Get dashboards from Orion
	public static Set<DashboardEntityBean> getDashboards(String categoryId) {
		String headerService = ConfOrionCB.getInstance().getString("orion.headers.service");
		String headerServicePath = ConfOrionCB.getInstance().getString("orion.headers.servicepath");

		Set<BasicNameValuePair> params = new HashSet<BasicNameValuePair>();
		params.add(new BasicNameValuePair("type", "dashboard_info"));
		if(categoryId != null) {
			params.add(new BasicNameValuePair("id",categoryId));
		}

		String orionResp = orion.getEntities(headerService, headerServicePath, params);

		Type type = new TypeToken<Set<DashboardEntityBean>>() {
		}.getType();

		Set<DashboardEntityBean> dashResult = new Gson().fromJson(orionResp, type);
		return dashResult;
	}
	// Get dashboards from persistence layer
	public static List<DashboardBean> getDashboardsFromPersistence(String categoryId) {
		String resp = "";
		try {
			resp = RestUtils.consumeGet(persistenceUrl + "/" + categoryId);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Type type = new TypeToken<CategoryBean>() {
		}.getType();

		CategoryBean result = new Gson().fromJson(resp, type);

		if(result == null) {
			return new ArrayList<DashboardBean>();
		} else {
			return result.getDashboardList();
		}
	}
	// Sets random coordinates in Europe (for testing)
	public static MapCenter getRandomMapCenter() {
		int latMax = 70;
		int latMin = 35;
		int lngMax = 70;
		int lngMin = -23;
		int latRange = latMax - latMin + 1;
		int lngRange = lngMax - lngMin + 1;
		int randomLat = (int)(Math.random() * latRange) + latMin;
		int	randomLng = (int)(Math.random() * lngRange) + lngMin;
		MapCenter mapcenter = new MapCenter();
		mapcenter.setLat(Double.valueOf(randomLat));
		mapcenter.setLng(Double.valueOf(randomLng));
		return mapcenter;
	}
	// Check if scope already exists on TenantManager
	public static Boolean contextExists(String context) {

		String resp = "";
		Boolean result = false;

		try {
			Map<String,String> headers = new HashMap<String,String>();
			KeycloakContext kc = new KeycloakContext();
			headers.put("Authorization", "Bearer " + kc.getServiceAccessToken());
			resp = RestUtils.consumeGet(tenantUrl + "/" + context, headers);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			Type type = new TypeToken<TenantScopeBean>() {
			}.getType();
			TenantScopeBean tenResp = new Gson().fromJson(resp, type);
			if(tenResp != null) {
				result = true;
			} else {
				result = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
