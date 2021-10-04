package it.eng.iot.servlet.model;

public class TokenBean {

	private String token ;
	private String refresh_token;
	private String validityDate;
	
	public TokenBean() {
		this("","","","");
	}
	
	public TokenBean(String token, String refreshToken, String username, String validityDate) {
		this.token = token;
		this.refresh_token = refreshToken;
		this.validityDate = validityDate;
	}
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getRefresh_token() {
		return refresh_token;
	}

	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}

	public String getValidityDate() {
		return validityDate;
	}

	public void setValidityDate(String validityDate) {
		this.validityDate = validityDate;
	}

}
