package net.stinfoservices.helias.renaud.tempest.tools;

public class NaNException extends Exception {

	/**
	 * UID.
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String MESSAGE = "vector contains a \"not a number\" value : Double.NaN";
	
	public NaNException() {
		super(MESSAGE);
	}

}
