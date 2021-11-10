package com.terturl.MMO.Dungeon.Exceptions;

public class LengthExceedsException extends Exception {

	private static final long serialVersionUID = 1L;

	public LengthExceedsException(Integer i) {
		super("Size exceeds 16 blocks: " + String.valueOf(i));
	}
	
}