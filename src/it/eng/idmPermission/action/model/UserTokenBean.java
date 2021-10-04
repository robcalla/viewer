package it.eng.idmPermission.action.model;

public class UserTokenBean {
	
	private Auth auth;

	public UserTokenBean(Auth auth) {
		super();
		this.auth = auth;
	}

	public Auth getAuth() {
		return auth;
	}

	public void setAuth(Auth auth) {
		this.auth = auth;
	}
	
}
