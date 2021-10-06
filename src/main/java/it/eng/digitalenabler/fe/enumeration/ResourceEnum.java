package it.eng.digitalenabler.fe.enumeration;

public enum ResourceEnum {
	CONTEXT("contex"),
	CATEGORY("category"),
	DASHBOARD("dashboard");
		
	private final String text;

	private ResourceEnum(final String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return this.text;
	}
	
}
