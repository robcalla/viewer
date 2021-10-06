package it.eng.digitalenabler.fe.permission.interfaces;

import it.eng.iot.configuration.ConfIDM;

public interface RoleTypeInterface {
	final static String user = ConfIDM.getInstance().getString("idm.role.user.name");
	final static String developer = ConfIDM.getInstance().getString("idm.role.developer.name");
	final static String admin = ConfIDM.getInstance().getString("idm.role.admin.name");
}
