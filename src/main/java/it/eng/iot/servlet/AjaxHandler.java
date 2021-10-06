package it.eng.iot.servlet;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;

import it.eng.digitalenabler.fe.enumeration.ResourceEnum;
import it.eng.digitalenabler.fe.permission.PermissionManager;
import it.eng.digitalenabler.fe.permission.Resource;
import it.eng.digitalenabler.fe.permission.ResourcePermission;
import it.eng.digitalenabler.keycloak.User;
import it.eng.iot.configuration.Conf;
import it.eng.iot.utils.CommonUtils;
import it.eng.iot.utils.ServiceConfigManager;
import it.eng.tools.model.DashboardBean;
import it.eng.tools.model.ScopeEntityBean;
import it.eng.tools.model.ScopeEntityBeanOverall;
import it.eng.tools.model.ServiceEntityBean;
import it.eng.tools.model.ServiceEntityBeanOverall;
import it.eng.tools.model.UrbSerEntityBean;

@WebServlet("/ajaxhandler")
public class AjaxHandler extends HttpServlet {
	private static final long serialVersionUID = -8956961196989629222L;

	private static final Logger LOGGER = Logger.getLogger(AjaxHandler.class.getName());
	private static final String defaultEnabler = Conf.getInstance().getString("MultiEnabler.default");
	private static final String destinationScope = Conf.getInstance().getString("ImageUpload.destinationScope");
	private static final String filePath = Conf.getInstance().getString("ImageUpload.path");
	private static Boolean isTenantActive = Boolean.valueOf(Conf.getInstance().getString("tenant.enabled"));
	
	public enum Ajaxaction {
		createContext("createContext"),
		createCategory("createCategory"), 
		getContexts("getContexts"),
		getCategories("getCategories"), 
		getContextName("getContextName"),
		getCategoryName("getCategoryName"), 
				
		getCockpitNames("getCockpitNames"), 
		setCockpitName("setCockpitName"), 
		
		deleteCategory("deleteCategory"), 
		deleteCategoriesByContextName("deleteCategoriesByContextName"),
		deleteContext("deleteContext"),
		appendDashboard("appendDashboard"), 
		removeDashboard("removeDashboard");

		private final String text;

		private Ajaxaction(final String text) {
			this.text = text;
		}

		public String toString() {
			return this.text;
		}

	};

	public AjaxHandler() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		Gson gson = new Gson();
		JSONObject jResp = new JSONObject();
		jResp.put("error", false);
				
		HttpSession session = request.getSession();

		try {
			String action = request.getParameter("action"); //$NON-NLS-1$
			Ajaxaction eAction = Ajaxaction.valueOf(action);
			
			switch (eAction) {
			
				case deleteCategoriesByContextName: {
					//Validate request					
					String context = (request.getParameter("context"));
					
					// Get user info 
					User userInfo = (User) session.getAttribute("userInfo");
					
					//Check if the user is authorized to delete dashboard from a context
					Boolean isAdmin = (boolean)session.getAttribute("userIsAdmin");
					
					PermissionManager pManager = new PermissionManager();
					Boolean serviceResponse;
					ResourcePermission perm;
					
					if(isAdmin) {
						perm = new ResourcePermission(ResourceEnum.CONTEXT, "CRUD");
					} else {
						Resource scopeResource = new Resource(ResourceEnum.CONTEXT,context);
						perm = pManager.getContextPermission(userInfo, scopeResource);
					}					
					
					if(!perm.getCanDelete()) serviceResponse = false;
					else serviceResponse = ServiceConfigManager.deleteCategoriesByContextName(context);
					
					jResp.put("orion_delete", serviceResponse);
					response.getWriter().write(jResp.toString());
					break;
				} 
				
				case deleteContext: {
					//Validate request					
					String context = (request.getParameter("context"));
					
					// Get user info 
					User userInfo = (User) session.getAttribute("userInfo");
					
					//Check if the user is authorized to delete dashboard from a context
					Boolean isAdmin = (boolean)session.getAttribute("userIsAdmin");
					
					PermissionManager pManager = new PermissionManager();
					Boolean serviceResponse;
					ResourcePermission perm;
			
					if(isAdmin) {
						perm = new ResourcePermission(ResourceEnum.CONTEXT, "CRUD");
					} else {
						//Check if the user is authorized to delete dashboard from a context
						Resource scopeResource = new Resource(ResourceEnum.CONTEXT,context);
						perm = pManager.getContextPermission(userInfo, scopeResource);
					}
					
					if(!perm.getCanDelete()) serviceResponse = false;
					else serviceResponse = ServiceConfigManager.deleteContext(context);	
					
					jResp.put("orion_delete", serviceResponse);
					response.getWriter().write(jResp.toString());
					//Delete the image if exists
					File dir;
					if("war".equals(destinationScope))
						dir = new File(getServletContext().getRealPath(filePath));
					else
						dir = new File(filePath);
					
					CommonUtils.deleteImageFromDir(dir, context);
					
					break;
				} 
				
				/* Create new urban service */
				case createCategory: {
					//Validate request
					String dashboardName = (request.getParameter("dashboardName"));
					String type = (request.getParameter("type"));
					String context = request.getHeader("fiware-service");
					String category = request.getHeader("fiware-servicepath");
					
					LOGGER.log(Level.INFO, category );
					
					String enabler = (String)request.getSession().getAttribute("enabler");
					
					if(enabler == null) {
						enabler = defaultEnabler;
					}
	
					boolean serviceResponse = ServiceConfigManager.createCategory(context, category, dashboardName, type, enabler);
					//orionj.put(serviceConfigBean.getId(), serviceResponse);
					jResp.put("orion_registration", serviceResponse);
					response.getWriter().write(jResp.toString());
					break;					
				}
				
				case createContext: {
					String contextName = request.getParameter("context");
					LOGGER.log(Level.INFO, contextName );
					String enabler = (String)request.getSession().getAttribute("enabler");
					if(enabler == null) {
						enabler = defaultEnabler;
					}
					
					//TODO: Check if the user is able to create the context
					boolean serviceResponse = ServiceConfigManager.createScope(contextName,enabler);
					if(!serviceResponse) {
						throw new Exception("Context already exists in another enabler!");
					} else {
						jResp.put("orion_registration", serviceResponse);
						response.getWriter().write(jResp.toString());
					}
					break;	
					
				}
				
				case getCategories:{
					try {
					String context = request.getParameter("context"); //$NON-NLS-1$
					
					// Get user info 
					User userInfo = (User) session.getAttribute("userInfo");
					//Check if the user can read the context
					Boolean isAdmin = (boolean)session.getAttribute("userIsAdmin");
										
					PermissionManager pManager = new PermissionManager();
					ResourcePermission perm;
					
					if(isAdmin) {
						perm = new ResourcePermission(ResourceEnum.CONTEXT, "CRUD");
					} else {
						Resource contextResource = new Resource(ResourceEnum.CONTEXT,context);
						perm = pManager.getContextPermission(userInfo, contextResource);
					}					
					
					if(!perm.getCanRead()) {
						response.getWriter().write(gson.toJson("[]"));
					}
					else {
						Set<ServiceEntityBean> categories = new HashSet<ServiceEntityBean>();
						Set<UrbSerEntityBean> tenantCategories = new HashSet<UrbSerEntityBean>();
						if(!isTenantActive) {
							categories = ServiceConfigManager.getCategoriesByContextName(context);
							response.getWriter().write(gson.toJson(categories));
						} else {
							tenantCategories = ServiceConfigManager.getTMCategories(context);
							response.getWriter().write(gson.toJson(tenantCategories));
						}
						
						
					}
					
					
					}
					catch (org.json.JSONException jsone) {
						jsone.printStackTrace();
						JSONArray emptyarr = new JSONArray();
						response.getWriter().write(emptyarr.toString());
						response.setStatus(500);
					}
					break;
				}
				
				case getContexts:{
					try {
						String scope = request.getParameter("enabler");
						Set<ScopeEntityBean> scopes = new HashSet<ScopeEntityBean>();
						Set<ServiceEntityBean> services = new HashSet<ServiceEntityBean>();
						if(isTenantActive) {
							scopes = ServiceConfigManager.getTMContexts(scope);
						} else {
							services = ServiceConfigManager.getServices(scope);
						}
						
						// Get user info 
						User userInfo = (User) session.getAttribute("userInfo");
						// Verify userIsAdmin 
						boolean userIsAdmin= true;
						if(session.getAttribute("userIsAdmin") != null){
							userIsAdmin = (Boolean) request.getSession().getAttribute("userIsAdmin");
						}
						
						//Merge with permission,dashboards and verticals information
						List<ServiceEntityBeanOverall> result = null;
						List<ScopeEntityBeanOverall> altResult = null;
						
						if (!userIsAdmin && !isTenantActive){		
							Map<ServiceEntityBean,ResourcePermission> permittedServices = ServiceConfigManager.getPermittedServices(services, userInfo);
							result = ServiceConfigManager.addServiceInfo(permittedServices);
						} else if (!userIsAdmin && isTenantActive) {
							Map<ScopeEntityBean, ResourcePermission> permittedScopes = ServiceConfigManager.getPermittedScopes(scopes, userInfo);
							altResult = ServiceConfigManager.addScopeInfo(permittedScopes, null);
						} else {
							ResourcePermission adminPermission= new ResourcePermission(ResourceEnum.CONTEXT, "CRUD");
							if(!isTenantActive) {
								result = ServiceConfigManager.addServiceInfo(services,adminPermission);
							} else {
								altResult = ServiceConfigManager.addScopeInfo(scopes,adminPermission);
							}
							
						}
						if(isTenantActive) {
							response.getWriter().write(gson.toJson(altResult));
						} else {
							response.getWriter().write(gson.toJson(result));
						}						
					}
					catch (org.json.JSONException jsone) {
						jsone.printStackTrace();
						JSONArray emptyarr = new JSONArray();
						response.getWriter().write(emptyarr.toString());
						response.setStatus(500);
					}
					break;
				}
				
				// Get the name of the scope
				case getContextName:{
					try {
						String id = request.getParameter("id");
						String serviceName = ServiceConfigManager.getContextName(id);
						
						response.getWriter().write(gson.toJson(serviceName));
					}
					catch (org.json.JSONException jsone) {
						jsone.printStackTrace();
						JSONArray emptyarr = new JSONArray();
						response.getWriter().write(emptyarr.toString());
						response.setStatus(500);
					}
					break;
				}
				// Get the name of the urbanservice
				case getCategoryName:{
					try {
						String refScope = request.getParameter("context");
						String categoryId = request.getParameter("category");
						String servicePathName = ServiceConfigManager.getCategoryName(categoryId, refScope);
						
						response.getWriter().write(gson.toJson(servicePathName));
					}
					catch (org.json.JSONException jsone) {
						jsone.printStackTrace();
						JSONArray emptyarr = new JSONArray();
						response.getWriter().write(emptyarr.toString());
						response.setStatus(500);
					}
					break;
				}
			
			
				case getCockpitNames : {
					//Validate request
					
					String category = (request.getParameter("category"));
					String context = (request.getParameter("context"));
					
					// Get user info 
					User userInfo = (User) session.getAttribute("userInfo");
					
					Boolean isAdmin = (boolean)session.getAttribute("userIsAdmin");
					PermissionManager pManager = new PermissionManager();
					ResourcePermission perm;
					
					//Check if the user can see dashboards
					if(isAdmin) {
						perm = new ResourcePermission(ResourceEnum.CONTEXT, "CRUD");
					}
					else {
						//Check if the user is authorized to delete dashboard from a context
						Resource scopeResource = new Resource(ResourceEnum.CONTEXT,context);
						perm = pManager.getContextPermission(userInfo, scopeResource);
						if(perm.getCanRead()) {
							//TODO In this version in order to obtain category permission you have to invoke the corrispective methods using as resource the scope
							perm = pManager.getCategoryPermission(userInfo, scopeResource);
							if(perm.getCanRead()) {
								perm = pManager.getDashboardPermission(userInfo, scopeResource);
							} else perm.setCanRead(false);
						} else perm.setCanRead(false);
					}
					JSONArray jsonResp = new JSONArray();
					
					if(perm.getCanRead()) {
							List<DashboardBean> dashboardList = ServiceConfigManager.getDashboardsFromPersistence(category);
							Set<DashboardBean> dashboardSet = new HashSet<>(dashboardList);
							jsonResp = new JSONArray(dashboardSet);
					}
					response.getWriter().write(jsonResp.toString());
					break;	
				}
				
				case setCockpitName : {
					//Validate request					
					String entityId = (request.getParameter("entityId"));
					String cockpitName = (request.getParameter("cockpitName"));
					String cockpitUrl = (request.getParameter("cockpitUrl"));
					String type = (request.getParameter("type"));
					Boolean serviceResponse = ServiceConfigManager.setCockpitName(entityId,cockpitName, cockpitUrl, type);
					jResp.put("orion_update", serviceResponse);
					response.getWriter().write(jResp.toString());
					break;
				}
				
				case appendDashboard : {
					//Validate request					
					String entityId = (request.getParameter("entityId"));
					String cockpitName = (request.getParameter("cockpitName"));
					String cockpitUrl = (request.getParameter("cockpitUrl"));
					String type = (request.getParameter("type"));
					Boolean serviceResponse = false;
					serviceResponse = ServiceConfigManager.appendCockpitFromPersistence(entityId, cockpitName, cockpitUrl, type);
					jResp.put("orion_update", serviceResponse);
					response.getWriter().write(jResp.toString());
					break;
				}
				
				case removeDashboard: {
					//Validate request					
					String entityId = (request.getParameter("entityId"));
					String cockpitName = (request.getParameter("cockpitName"));
					
					//TODO: Check if the user is authorized to delete dashboard
					
					Boolean serviceResponse = false;
					serviceResponse = ServiceConfigManager.removeDashboardFromPersistence(entityId, cockpitName);
					jResp.put("orion_update", serviceResponse);
					response.getWriter().write(jResp.toString());
					break;
				}
				
				case deleteCategory : {
					//Validate request					
					String context = (request.getParameter("context"));
					String category = (request.getParameter("category"));
					
					//Check if the user is authorized to delete category
					
					// Get user info 
					User userInfo = (User) session.getAttribute("userInfo");
					
					Boolean isAdmin = (boolean)session.getAttribute("userIsAdmin");
					PermissionManager pManager = new PermissionManager();
					ResourcePermission perm;
					Boolean serviceResponse;
				
					if(isAdmin) {
						perm = new ResourcePermission(ResourceEnum.CONTEXT, "CRUD");
					}
					else {
						//Check if the user is authorized to delete dashboard from a context
						Resource scopeResource = new Resource(ResourceEnum.CONTEXT,context);
						perm = pManager.getContextPermission(userInfo, scopeResource);
						if(perm.getCanRead())
							//TODO In this version in order to obtain category permission you have to invoke the corrispective methods using as resource the scope
							perm = pManager.getCategoryPermission(userInfo, scopeResource);
						else perm.setCanDelete(false);
					}
					String entityId = context + "_" + category;
					
					if(!perm.getCanDelete()) serviceResponse = false;
					else {
						serviceResponse = ServiceConfigManager.deleteAllPersistedDashboards(entityId);

					}
					
					jResp.put("orion_update", serviceResponse);
					response.getWriter().write(jResp.toString());
					break;
				}
				
			default:
					throw new Exception("Unsupported action " + action); //$NON-NLS-1$
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			
			LOGGER.log(Level.INFO,e.getMessage());
			response.setStatus(500);

			jResp = new JSONObject();
			jResp.put("error", true); //$NON-NLS-1$
			jResp.put("message", e.getMessage().toString()); //$NON-NLS-1$
			
			response.getWriter().write(jResp.toString());
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
