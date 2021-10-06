package it.eng.digitalenabler.fe.enumeration;

import it.eng.digitalenabler.fe.permission.interfaces.RoleTypeInterface;

public enum RoleTypeEnum implements RoleTypeInterface{
	
	USER(user),
	DEVELOPER(developer),
	ADMIN(admin);
		
	private final String text;
	
	
	private RoleTypeEnum(final String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return this.text;
	}
	
}
