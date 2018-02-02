package com.fwcd.amethyst.exception;

public class AmethystException extends RuntimeException {
	private static final long serialVersionUID = -3549405454731382087L;

	public AmethystException(Throwable cause) {
		super(cause);
	}
	
	public AmethystException(String reason) {
		super(reason);
	}
}
