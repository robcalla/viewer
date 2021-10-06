package it.eng.iot.servlet.model;



// #ApplicationRoles.OrganizationsRole.Asset.AssetRole=Permission
public class Permission {

	
	private String applicationRole;
	private String organizationsRole;
	private String asset;
	private String assetRole;
	private String permissionCRUD;
	
	

	public String getApplicationRole() {
		return applicationRole;
	}

	public void setApplicationRole(String applicationRole) {
		this.applicationRole = applicationRole;
	}

	public String getOrganizationsRole() {
		return organizationsRole;
	}

	public void setOrganizationsRole(String organizationsRole) {
		this.organizationsRole = organizationsRole;
	}

	public String getAsset() {
		return asset;
	}

	public void setAsset(String asset) {
		this.asset = asset;
	}

	public String getAssetRole() {
		return assetRole;
	}

	public void setAssetRole(String assetRole) {
		this.assetRole = assetRole;
	}

	public String getPermissionCRUD() {
		return permissionCRUD;
	}

	public void setPermissionCRUD(String permissionCRUD) {
		this.permissionCRUD = permissionCRUD;
	}

	public Permission() {
		
		applicationRole = ""; 
		organizationsRole = "";
		asset = "";
		assetRole = "";
		permissionCRUD = "";
		
	}

	public Permission(String applicationRole, String organizationsRole, String asset, String assetRole, String permissionCRUD ) {
		this.applicationRole = applicationRole;
		this.organizationsRole = organizationsRole;
		this.asset = asset;
		this.assetRole = assetRole;
		this.permissionCRUD = permissionCRUD;
	}
	
	
	

	

}
