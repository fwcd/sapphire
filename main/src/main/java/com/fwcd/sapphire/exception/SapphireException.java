package com.fwcd.sapphire.exception;

public class SapphireException extends RuntimeException {
	private static final long serialVersionUID = -3549405454731382087L;

	public SapphireException(Throwable cause) {
		super(cause);
	}
	
	public SapphireException(String reason) {
		super(reason);
	}
}
