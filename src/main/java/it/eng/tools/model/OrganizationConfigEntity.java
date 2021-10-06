package it.eng.tools.model;
/*
 * 
 *  {
        "id": "0888009313824014b81d94f9b6c1c4d3",
        "type": "organization",
        "apikey": {
            "type": "Text",
            "value": "GmJszisVlK54sGC",
            "metadata": {}
        },
        "description": {
            "type": "Text",
            "value": "aaaa",
            "metadata": {}
        },
        "name": {
            "type": "Text",
            "value": "RAFFA",
            "metadata": {}
        },
        "opType": {
            "type": "Text",
            "value": "created",
            "metadata": {}
        }
    }
 */

public  class OrganizationConfigEntity extends CBEntity{
	
	private EntityAttribute<String> apikey;
	private EntityAttribute<String> name;
	private EntityAttribute<String> description; 
	private EntityAttribute<String> opType;
	
	
	
	public EntityAttribute<String> getApikey() {
		return apikey;
	}
	public void setApikey(EntityAttribute<String> apikey) {
		this.apikey = apikey;
	}


	public EntityAttribute<String> getName() {
		return name;
	}
	public void setName(EntityAttribute<String> name) {
		this.name = name;
	}


	public EntityAttribute<String> getDescription() {
		return description;
	}
	public void setDescription(EntityAttribute<String> description) {
		this.description = description;
	}


	public EntityAttribute<String> getOpType() {
		return opType;
	}
	public void setOpType(EntityAttribute<String> opType) {
		this.opType = opType;
	}


	public OrganizationConfigEntity() {
		super(new String(), new String());
		new EntityAttribute<String>(new String());
		new EntityAttribute<String>(new String());
		new EntityAttribute<String>(new String());
		new EntityAttribute<String>(new String());
	}

	public OrganizationConfigEntity(String id, String type, EntityAttribute<String> apikey, EntityAttribute<String> name, EntityAttribute<String> description, EntityAttribute<String> opType) {
		super(id, type);
		this.apikey = apikey;
		this.name = name;
		this.description = description;
		this.opType = opType;
		
	}
	
	
	
	
	
	
}
