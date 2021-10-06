package it.eng.iot.exceptions;

public class ForbiddenException extends RuntimeException {
	private static final long serialVersionUID = 2148364608343881555L;

	public ForbiddenException(String message){
		super(message);
	}
}
