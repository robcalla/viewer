package it.eng.tools.base;

public abstract class IdentityManager extends Tool {

	protected IdentityManager(String baseurl) throws Exception{
		super(baseurl);
	}
	
	protected abstract String getUserInfo(String token) throws Exception;
	
}
