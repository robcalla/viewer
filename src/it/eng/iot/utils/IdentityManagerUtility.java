package it.eng.iot.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.eng.digitalenabler.fe.enumeration.ResourceEnum;
import it.eng.digitalenabler.fe.permission.PermissionManager;
import it.eng.digitalenabler.fe.permission.Resource;
import it.eng.digitalenabler.fe.permission.ResourcePermission;
import it.eng.digitalenabler.fiware.keyrock7.dto.UserDTO;
import it.eng.digitalenabler.identity.manager.model.Organization;
import it.eng.digitalenabler.identity.manager.model.Role;
import it.eng.digitalenabler.identity.manager.model.Token;
import it.eng.digitalenabler.idm.fiware.IdentityManager;
import it.eng.digitalenabler.keycloak.User;
import it.eng.iot.configuration.Conf;
import it.eng.iot.configuration.ConfIDM;
import it.eng.iot.servlet.model.Permission;
import it.eng.tools.LogFilter;
import it.eng.tools.model.ServiceEntityBean;
import it.eng.tools.model.UrbSerEntityBean;

public class IdentityManagerUtility {

	private static IdentityManager idm = null;
	private static final Logger LOGGER = Logger.getLogger(IdentityManagerUtility.class.getName());
	
	static {
		LogFilter logFilter = new LogFilter();
		LOGGER.setFilter(logFilter);
	}

	private IdentityManagerUtility() {
	}

	public static IdentityManager getIdentityManager() {
		return IdentityManagerUtility.idm;
	}

	public static void setIdentityManager(IdentityManager idm) {
		IdentityManagerUtility.idm = idm;
	}

	public static boolean checkToken(String token) {
		boolean isvalidtoken = true;

		try {
			LOGGER.log(Level.INFO, "User Access Token: " + token);
			isvalidtoken = IdentityManagerUtility.getIdentityManager().checkToken(token);
		} catch (Exception e) {
			isvalidtoken = false;
		}

		return isvalidtoken;
	}

	public static Token refreshToken(String refresh_token) {

		String client_id = ConfIDM.getInstance().getString("idm.client.id");
		String client_secret = ConfIDM.getInstance().getString("idm.client.secret");

		Token tb = new Token();

		try {
			tb = IdentityManagerUtility.getIdentityManager().refreshToken(refresh_token, client_id, client_secret);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
		}
		return tb;
	}
	
	public static ResourcePermission getAssetPermission(User userInfo, ResourceEnum assetType) {
		return getAssetPermission(userInfo,assetType,null);
	}
	
	public static ResourcePermission getAssetPermission(User userInfo, ResourceEnum assetType, Resource refersTo) {
		PermissionManager permManager = new PermissionManager();
		ResourcePermission resourcePerm = permManager.getAssetPermission(userInfo, assetType,refersTo);
		return resourcePerm;
	}
	
	public static Map<String,ResourcePermission> getUserContextsPermissions(User userInfo, Set<ServiceEntityBean> contexts) {
		Map<String,ResourcePermission> contextsPerms = new HashMap<String,ResourcePermission>();
		for(ServiceEntityBean context : contexts) {
			String contextId = context.getId();
			PermissionManager permManager = new PermissionManager();
			Resource requestedResource = new Resource(ResourceEnum.CONTEXT, contextId);
			ResourcePermission resourcePerm = permManager.getContextPermission(userInfo, requestedResource);
			if(resourcePerm!=null)
				contextsPerms.put(contextId,resourcePerm);
		}
		return contextsPerms;
	};
	
	public static Map<String,ResourcePermission> getUserCategoriesPermissions(User userInfo, Set<UrbSerEntityBean> categories) {
		Map<String,ResourcePermission> categoriesPerms = new HashMap<String,ResourcePermission>();
		for(UrbSerEntityBean category : categories) {
			String contextId = category.getId();
			PermissionManager permManager = new PermissionManager();
			Resource requestedResource = new Resource(ResourceEnum.CATEGORY, contextId);
			ResourcePermission resourcePerm = permManager.getContextPermission(userInfo, requestedResource);
			if(resourcePerm!=null)
				categoriesPerms.put(contextId,resourcePerm);
		}
		return categoriesPerms;
	};
	
	public static Map<String,ResourcePermission> getUserDashboardsPermissions(User userInfo, Set<String> dashboards) {
		Map<String,ResourcePermission> categoriesPerms = new HashMap<String,ResourcePermission>();
		for(String dashboardId : dashboards) {
			PermissionManager permManager = new PermissionManager();
			Resource requestedResource = new Resource(ResourceEnum.CATEGORY, dashboardId);
			ResourcePermission resourcePerm = permManager.getContextPermission(userInfo, requestedResource);
			if(resourcePerm!=null)
				categoriesPerms.put(dashboardId,resourcePerm);
		}
		return categoriesPerms;
	};

	@Deprecated
	public static Set<Permission> getUserPermissions(it.eng.digitalenabler.identity.manager.model.User user) {

		// 1. Load configuration permission
		LOGGER.log(Level.INFO, "=== getUserPermissions ===");

		Permission userPerm;
		Set<Permission> userPermSet = new HashSet<Permission>();
		Set<Permission> configPermissions = loadConfigPermissions();

		// 2. ROLE AND PERMISSION OF THE CONNECTED USER
		String userId = user.getId();
		LOGGER.log(Level.INFO, "CONNECTED USER ID: " + userId);

		// 2.2. RUOLI INTERNI ALL'ORGANIZZAZIONE
		Set<Organization> orgs = user.getOrganizationsRoles().get().keySet();
		LOGGER.log(Level.INFO, "Number of user organization: " + orgs.size());
		for (Organization org : orgs) {
			LOGGER.log(Level.INFO, "Organization >>> " + org.getName() + " | " + org.getId());
			String organizationId = org.getId();
			Map<String, List<Role>> userOrgRolesMap = user.getOrganizationsRoles().get().get(org);
			List<Role> userOrgRoles = userOrgRolesMap.get(UserDTO.ORGANIZATION_ROLE);//Ruoli nell'organization
			String userOrganizationRole = userOrgRolesMap.get(UserDTO.MEMBERSHIP_TYPE).get(0).getName(); // Membership dell'organization
			
			LOGGER.log(Level.INFO, "User: " + userId + " OrganizationId: " + organizationId
					+ " User Organization Role: " + userOrganizationRole);
			
			//FIX IDM7 Add external permission (Seller,Citizen)
			userOrgRoles.addAll(user.getRoles().get());
			
			for (Role orgUserRole : userOrgRoles) {
				LOGGER.log(Level.INFO, "Connected user organization role: " + orgUserRole.getName());
				String roleName = orgUserRole.getName().trim().replaceAll("\\s", "").toLowerCase();
				// GET THE CORRESPONDING PERMISSION
				for (Permission perm : configPermissions) {
					// String assetRole = "owner"; // GET FROM API
					if (perm.getApplicationRole().equalsIgnoreCase(roleName)
							&& perm.getOrganizationsRole().equalsIgnoreCase(userOrganizationRole)) {
						userPerm = new Permission(); // svuoto
						userPerm.setPermissionCRUD(perm.getPermissionCRUD());
						userPerm.setApplicationRole(perm.getApplicationRole());
						userPerm.setOrganizationsRole(perm.getOrganizationsRole());
						userPerm.setAsset(perm.getAsset());
						userPerm.setAssetRole(perm.getAssetRole());
						LOGGER.log(Level.INFO, "user role: " + roleName + " permissions " + userPerm.getPermissionCRUD()
								+ " " + userPerm.getAsset() + " is asset owner: " + userPerm.getAssetRole());
						// LOAD THE Permission IN THE PERMISSIONS
						userPermSet.add(userPerm);
					}

				}

				LOGGER.log(Level.INFO, "user role: " + roleName);
				LOGGER.log(Level.INFO, "userPerms: " + userPermSet);

			}
		}
		// RETURN TO THE FRONT END THE CONNECTED USER PERMISSION
		// LOGGER.log(Level.INFO, "Number of permission returned: " +
		// userPermSet.size());
		return userPermSet;

	}

	/*
	 * Loads configuration permissions
	 */
	@Deprecated
	public static Set<Permission> loadConfigPermissions() {

		Set<Permission> configPermissions = new HashSet<Permission>();

		// Seller
		// CASE: seller.owner.scope.owner=CRUD
		Permission permission = new Permission();
		permission.setApplicationRole("seller");
		permission.setOrganizationsRole("owner");
		permission.setAsset("scope");
		permission.setAssetRole("owner");
		permission.setPermissionCRUD(Conf.getInstance().getString("seller.owner.scope.owner"));
		configPermissions.add(permission);

		// CASE: seller.owner.urbanservice.owner=CRUD
		permission = new Permission();
		permission.setApplicationRole("seller");
		permission.setOrganizationsRole("owner");
		permission.setAsset("urbanservice");
		permission.setAssetRole("owner");
		permission.setPermissionCRUD(Conf.getInstance().getString("seller.owner.urbanservice.owner"));
		configPermissions.add(permission);

		// CASE: seller.owner.device.owner=CRUD
		permission = new Permission(); // svuoto
		permission.setApplicationRole("seller");
		permission.setOrganizationsRole("owner");
		permission.setAsset("device");
		permission.setAssetRole("owner");
		permission.setPermissionCRUD(Conf.getInstance().getString("seller.owner.device.owner"));
		configPermissions.add(permission);

		// FIX
		// Seller - member
		// CASE: seller.member.scope.owner=CRUD
		permission = new Permission(); // svuoto
		permission.setApplicationRole("seller");
		permission.setOrganizationsRole("member");
		permission.setAsset("scope");
		permission.setAssetRole("owner");
		permission.setPermissionCRUD(Conf.getInstance().getString("seller.member.scope.owner"));
		configPermissions.add(permission);

		// CASE: seller.member.urbanservice.owner=CRUD
		permission = new Permission();
		permission.setApplicationRole("seller");
		permission.setOrganizationsRole("member");
		permission.setAsset("urbanservice");
		permission.setAssetRole("owner");
		permission.setPermissionCRUD(Conf.getInstance().getString("seller.member.urbanservice.owner"));
		configPermissions.add(permission);

		// CASE: seller.member.device.owner=CRUD
		permission = new Permission(); // svuoto
		permission.setApplicationRole("seller");
		permission.setOrganizationsRole("member");
		permission.setAsset("device");
		permission.setAssetRole("owner");
		permission.setPermissionCRUD(Conf.getInstance().getString("seller.member.device.owner"));
		configPermissions.add(permission);

		// ********************************
		// #cityManager OWNER
		// CASE: citymanager.owner.scope.owner=CRUD
		permission = new Permission(); // svuoto
		permission.setApplicationRole("citymanager");
		permission.setOrganizationsRole("owner");
		permission.setAsset("scope");
		permission.setAssetRole("owner");
		permission.setPermissionCRUD(Conf.getInstance().getString("citymanager.owner.scope.owner"));
		configPermissions.add(permission);

		// CASE: citymanager.owner.urbanservice.owner=CRUD
		permission = new Permission(); // svuoto
		permission.setApplicationRole("citymanager");
		permission.setOrganizationsRole("owner");
		permission.setAsset("urbanservice");
		permission.setAssetRole("owner");
		permission.setPermissionCRUD(Conf.getInstance().getString("citymanager.owner.urbanservice.owner"));
		configPermissions.add(permission);

		// CASE: citymanager.owner.device.owner=CRUD
		permission = new Permission();
		permission.setApplicationRole("citymanager");
		permission.setOrganizationsRole("owner");
		permission.setAsset("device");
		permission.setAssetRole("owner");
		permission.setPermissionCRUD(Conf.getInstance().getString("citymanager.owner.device.owner"));
		configPermissions.add(permission);

		// #cityManager MEMBER and Asset OWNER
		// CASE: citymanager.member.scope.owner=CRUD
		permission = new Permission();
		permission.setApplicationRole("citymanager");
		permission.setOrganizationsRole("member");
		permission.setAsset("scope");
		permission.setAssetRole("owner");
		permission.setPermissionCRUD(Conf.getInstance().getString("citymanager.member.scope.owner"));
		configPermissions.add(permission);

		// CASE: citymanager.member.urbanservice.owner=CRUD
		permission = new Permission();
		permission.setApplicationRole("citymanager");
		permission.setOrganizationsRole("member");
		permission.setAsset("urbanservice");
		permission.setAssetRole("owner");
		permission.setPermissionCRUD(Conf.getInstance().getString("citymanager.member.urbanservice.owner"));
		configPermissions.add(permission);

		// CASE: citymanager.member.device.owner=CRUD
		permission = new Permission();
		permission.setApplicationRole("citymanager");
		permission.setOrganizationsRole("member");
		permission.setAsset("device");
		permission.setAssetRole("owner");
		permission.setPermissionCRUD(Conf.getInstance().getString("citymanager.member.device.owner"));
		configPermissions.add(permission);

		// #cityManager MEMBER and Asset NOT OWNER
		// CASE: citymanager.member.scope.member=R
		permission = new Permission();
		permission.setApplicationRole("citymanager");
		permission.setOrganizationsRole("member");
		permission.setAsset("scope");
		permission.setAssetRole("member");
		permission.setPermissionCRUD(Conf.getInstance().getString("citymanager.member.scope.member"));
		configPermissions.add(permission);

		// CASE: citymanager.member.urbanservice.member=R
		permission = new Permission();
		permission.setApplicationRole("citymanager");
		permission.setOrganizationsRole("member");
		permission.setAsset("urbanservice");
		permission.setAssetRole("member");
		permission.setPermissionCRUD(Conf.getInstance().getString("citymanager.member.urbanservice.member"));
		configPermissions.add(permission);

		// CASE: citymanager.member.device.member=R
		permission = new Permission();
		permission.setApplicationRole("citymanager");
		permission.setOrganizationsRole("member");
		permission.setAsset("device");
		permission.setAssetRole("member");
		permission.setPermissionCRUD(Conf.getInstance().getString("citymanager.member.device.member"));
		configPermissions.add(permission);

		// ********************************
		// #urbanserviceProvider OWNER
		// CASE: urbanserviceprovider.owner.scope.owner=R
		permission = new Permission();
		permission.setApplicationRole("urbanserviceprovider");
		permission.setOrganizationsRole("owner");
		permission.setAsset("scope");
		permission.setAssetRole("owner");
		permission.setPermissionCRUD(Conf.getInstance().getString("urbanserviceprovider.owner.scope.owner"));
		configPermissions.add(permission);

		// CASE: urbanserviceprovider.owner.urbanservice.owner=R
		permission = new Permission(); // svuoto
		permission.setApplicationRole("urbanserviceprovider");
		permission.setOrganizationsRole("owner");
		permission.setAsset("urbanservice");
		permission.setAssetRole("owner");
		permission.setPermissionCRUD(Conf.getInstance().getString("urbanserviceprovider.owner.urbanservice.owner"));
		configPermissions.add(permission);

		// CASE: urbanserviceprovider.owner.device.owner=CRUD
		permission = new Permission();
		permission.setApplicationRole("urbanserviceprovider");
		permission.setOrganizationsRole("owner");
		permission.setAsset("device");
		permission.setAssetRole("owner");
		permission.setPermissionCRUD(Conf.getInstance().getString("urbanserviceprovider.owner.device.owner"));
		configPermissions.add(permission);

		// #cityManager MEMBER and Asset OWNER
		// CASE: urbanserviceprovider.member.scope.owner=R
		permission = new Permission();
		permission.setApplicationRole("urbanserviceprovider");
		permission.setOrganizationsRole("member");
		permission.setAsset("scope");
		permission.setAssetRole("owner");
		permission.setPermissionCRUD(Conf.getInstance().getString("urbanserviceprovider.member.scope.owner"));
		configPermissions.add(permission);

		// CASE: urbanserviceprovider.member.urbanservice.owner=R
		permission = new Permission();
		permission.setApplicationRole("urbanserviceprovider");
		permission.setOrganizationsRole("member");
		permission.setAsset("urbanservice");
		permission.setAssetRole("owner");
		permission.setPermissionCRUD(Conf.getInstance().getString("urbanserviceprovider.member.urbanservice.owner"));
		configPermissions.add(permission);

		// CASE: urbanserviceprovider.member.device.owner=CRU
		permission = new Permission();
		permission.setApplicationRole("urbanserviceprovider");
		permission.setOrganizationsRole("member");
		permission.setAsset("device");
		permission.setAssetRole("owner");
		permission.setPermissionCRUD(Conf.getInstance().getString("urbanserviceprovider.member.device.owner"));
		configPermissions.add(permission);

		// #cityManager MEMBER and Asset NOT OWNER
		// CASE: urbanserviceprovider.member.scope.member=R
		permission = new Permission();
		permission.setApplicationRole("urbanserviceprovider");
		permission.setOrganizationsRole("member");
		permission.setAsset("scope");
		permission.setAssetRole("member");
		permission.setPermissionCRUD(Conf.getInstance().getString("urbanserviceprovider.member.scope.member"));
		configPermissions.add(permission);

		// CASE: urbanserviceprovider.member.urbanservice.member=R
		permission = new Permission();
		permission.setApplicationRole("urbanserviceprovider");
		permission.setOrganizationsRole("member");
		permission.setAsset("urbanservice");
		permission.setAssetRole("member");
		permission.setPermissionCRUD(Conf.getInstance().getString("urbanserviceprovider.member.urbanservice.member"));
		configPermissions.add(permission);

		// CASE: urbanserviceprovider.member.device.member=R
		permission = new Permission();
		permission.setApplicationRole("urbanserviceprovider");
		permission.setOrganizationsRole("member");
		permission.setAsset("device");
		permission.setAssetRole("member");
		permission.setPermissionCRUD(Conf.getInstance().getString("urbanserviceprovider.member.device.member"));
		configPermissions.add(permission);

		LOGGER.log(Level.INFO, "Loaded configured permissions " + configPermissions.size());
		return configPermissions;

	}

	


}
