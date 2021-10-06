package it.eng.digitalenabler.fe.permission.interfaces;

import it.eng.digitalenabler.fe.permission.Resource;
import it.eng.digitalenabler.fe.permission.ResourcePermission;
//import it.eng.digitalenabler.identity.manager.model.User;
import it.eng.digitalenabler.keycloak.User;

public interface PermissionManagerInterface {
	public  ResourcePermission getContextPermission(User user,Resource requestedResult);
	public  ResourcePermission getCategoryPermission(User user,Resource requestedResult);
	public  ResourcePermission getDashboardPermission(User user,Resource requestedResult);
}
