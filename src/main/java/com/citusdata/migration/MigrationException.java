/**
 * 
 */
package com.citusdata.migration;

public abstract class MigrationException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7432723543474028751L;

	public MigrationException(Exception e) {
		super(e);
	}

	public MigrationException(String message, Object... args) {
		super(String.format(message, args));
	}


}
