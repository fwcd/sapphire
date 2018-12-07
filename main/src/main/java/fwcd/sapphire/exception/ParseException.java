package fwcd.sapphire.exception;

public class ParseException extends SapphireException {
	private static final long serialVersionUID = 6160098715998041389L;
	
	public ParseException(String reason) {
		super(reason);
	}
}
