package it.eng.iot.servlet.model;

public enum BusinessDataModel {
	PARKING("parking"),
	WASTE("waste"), 
	MOBILITY("mobility"), 
	ILLUMINATION("illumination"),
	ENVIRONMENT("environment"),
	TOURISM("tourism"),
	WATER("water"),
	TRIBUTES("tributes");
	
	private final String text;

	private BusinessDataModel(final String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return this.text;
	}
	
}
