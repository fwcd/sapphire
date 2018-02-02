package com.fwcd.amethyst.exception;

public class ParseException extends AmethystException {
	private static final long serialVersionUID = 6160098715998041389L;
	
	public ParseException(String reason) {
		super(reason);
	}
}
