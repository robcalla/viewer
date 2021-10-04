package it.eng.iot.exceptions;

public class UnauthorizedException extends RuntimeException {
	private static final long serialVersionUID = -5527508496158678787L;

	public UnauthorizedException(String message){
		super(message);
	}
}
