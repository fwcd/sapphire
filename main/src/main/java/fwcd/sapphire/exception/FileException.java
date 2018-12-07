package fwcd.sapphire.exception;

public class FileException extends SapphireException {
	private static final long serialVersionUID = 8723492734982734987L;
	
	public FileException(Throwable cause) {
		super(cause);
	}
	
	public FileException(String reason) {
		super(reason);
	}
}
