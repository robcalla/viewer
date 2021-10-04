package it.eng.idmPermission.action.model;

public class Auth {
	
	private Identity identity;

	public Auth(Identity identity) {
		super();
		this.identity = identity;
	}

	public Identity getIdentity() {
		return identity;
	}

	public void setIdentity(Identity identity) {
		this.identity = identity;
	}
	
}
