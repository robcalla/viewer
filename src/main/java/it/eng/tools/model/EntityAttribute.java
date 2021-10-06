package it.eng.tools.model;

import java.util.Collection;

public class EntityAttribute<T> {

	private T value;
	private String type;
	
	public EntityAttribute(T value) {
		setValue(value);
		if(this.value instanceof Collection)
			setType("List");
		else
			setType(this.value.getClass().getSimpleName());
	}
	
	public T getValue() {
		return value;
	}
	public void setValue(T value) {
		this.value = value;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
