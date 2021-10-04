package it.eng.digitalenabler.fe.permission;

import it.eng.digitalenabler.fe.enumeration.ResourceEnum;

public class ResourcePermission {
	private ResourceEnum type;
	private Boolean canCreate;
	private Boolean canRead;
	private Boolean canUpdate;
	private Boolean canDelete;
	
	public ResourcePermission(ResourceEnum type, Boolean canCreate, Boolean canRead, Boolean canUpdate,
			Boolean canDelete) {
		super();
		this.type = type;
		this.canCreate = canCreate;
		this.canRead = canRead;
		this.canUpdate = canUpdate;
		this.canDelete = canDelete;
	}
	
	public ResourcePermission(ResourceEnum type,String crudString) {
		super();
		this.type = type;
		this.canCreate = (crudString.contains("C")) ? true : false;
		this.canRead = (crudString.contains("R")) ? true : false;
		this.canUpdate = (crudString.contains("U")) ? true : false;
		this.canDelete = (crudString.contains("D")) ? true : false;
	}

	public ResourceEnum getType() {
		return type;
	}

	public void setType(ResourceEnum type) {
		this.type = type;
	}

	public Boolean getCanCreate() {
		return canCreate;
	}

	public void setCanCreate(Boolean canCreate) {
		this.canCreate = canCreate;
	}

	public Boolean getCanRead() {
		return canRead;
	}

	public void setCanRead(Boolean canRead) {
		this.canRead = canRead;
	}

	public Boolean getCanUpdate() {
		return canUpdate;
	}

	public void setCanUpdate(Boolean canUpdate) {
		this.canUpdate = canUpdate;
	}

	public Boolean getCanDelete() {
		return canDelete;
	}

	public void setCanDelete(Boolean canDelete) {
		this.canDelete = canDelete;
	}

}
