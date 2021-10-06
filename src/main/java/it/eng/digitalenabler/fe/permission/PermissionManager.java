package it.eng.digitalenabler.fe.permission;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import it.eng.digitalenabler.fe.enumeration.ResourceEnum;
import it.eng.digitalenabler.fe.permission.interfaces.PermissionManagerInterface;
import it.eng.digitalenabler.keycloak.User;

public class PermissionManager implements PermissionManagerInterface {
	
	public ResourcePermission getAssetPermission(User user, ResourceEnum resourceType) {
		return getAssetPermission(user,resourceType,null);
	}

	//Check the user's asset permission evaluating the role that the user has in each organization 
	public ResourcePermission getAssetPermission(User user, ResourceEnum resourceType, Resource refersTo) {
		List<ResourcePermission> permissionsByOrganization = new ArrayList<ResourcePermission>();
		
		Set<String> roles = user.getRoles().get();
		Set<String> organizations = user.getOrganizations().get()
				.stream()
		        .map(String::toLowerCase)
		        .collect(Collectors.toSet());
		Set<String> groups = user.getGroups().get();	

		if(!groups.isEmpty()) {
			for (String group : groups) {
				ResourcePermission rPerm = null;
				String permission = "";
				boolean isOwner = organizations.contains(group);
				if(isOwner || roles.contains("developer")) {
					permission += "CU";
				} else {
					permission += "R";
				}
				
				if(roles.contains("admin")) {
					permission += "D";
				}
				
				rPerm = new ResourcePermission(ResourceEnum.CONTEXT, permission);

				if (rPerm != null) {
					permissionsByOrganization.add(rPerm);
				}
								
			}
		}
		
		return this.getMaximumPermission(permissionsByOrganization, resourceType);

	}

	private ResourcePermission getResourcePermission(User user, Resource requestedResource, ResourceEnum resourceType) {

		List<ResourcePermission> permissionsByOrganization = new ArrayList<ResourcePermission>();
		
		Set<String> roles = user.getRoles().get();
		Set<String> organizations = user.getOrganizations().get()
				.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
		Set<String> groups = user.getGroups().get();	
		
		if(!groups.isEmpty()) {
			for (String group : groups) {
				ResourcePermission rPerm = null;
				String permission = "";
				boolean isOwner = organizations.contains(group);
				if(isOwner || roles.contains("developer")) {
					permission += "CU";
				} 
												
				String permissionName = requestedResource.getId();
								
				if (!group.equalsIgnoreCase(permissionName))
					continue;
				permission += "R";
				
				if(roles.contains("admin")) {
					permission += "D";
				}
				
				rPerm = new ResourcePermission(ResourceEnum.CONTEXT, permission);

				if (rPerm != null) {
					permissionsByOrganization.add(rPerm);
				}
								
			}
		}
		
		return this.getMaximumPermission(permissionsByOrganization, resourceType);

	}
	
	private ResourcePermission getMaximumPermission(List<ResourcePermission> perms, ResourceEnum resourceType) {
		Boolean canCreate = false;
		Boolean canRead = false;
		Boolean canUpdate = false;
		Boolean canDelete = false;

		for (ResourcePermission p : perms) {
			if (!canCreate && p.getCanCreate())
				canCreate = true;
			if (!canRead && p.getCanRead())
				canRead = true;
			if (!canUpdate && p.getCanUpdate())
				canUpdate = true;
			if (!canDelete && p.getCanDelete())
				canDelete = true;
		}

		return new ResourcePermission(resourceType, canCreate, canRead, canUpdate, canDelete);
	}

	@Override
	public ResourcePermission getContextPermission(User user, Resource requestedResource) {
		ResourceEnum CONTEXT = ResourceEnum.CONTEXT;
		return getResourcePermission(user, requestedResource, CONTEXT);
	}

	@Override
	public ResourcePermission getCategoryPermission(User user, Resource requestedResource) {
		ResourceEnum CATEGORY = ResourceEnum.CATEGORY;
		return getResourcePermission(user, requestedResource, CATEGORY);
	}

	@Override
	public ResourcePermission getDashboardPermission(User user, Resource requestedResource) {
		ResourceEnum DASHBOARD = ResourceEnum.DASHBOARD;
		return getResourcePermission(user, requestedResource, DASHBOARD);
	}

	public ResourcePermission getPublicContextPermission(User user, Resource requestedResource) {
		return null;
	}

	public ResourcePermission getPrivateContextPermission(User user, Resource requestedResource) {
		return null;
	}

	public ResourcePermission getPublicCategoryPermission(User user, Resource requestedResource) {
		return null;
	}

	public ResourcePermission getPrivateCategoryPermission(User user, Resource requestedResource) {
		return null;
	}

	public ResourcePermission getPublicDashboardPermission(User user, Resource requestedResource) {
		return null;
	}

	public ResourcePermission getPrivateDashboardPermission(User user, Resource requestedResource) {
		return null;
	}

}
