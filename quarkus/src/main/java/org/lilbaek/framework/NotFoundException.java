package org.lilbaek.framework;

public class NotFoundException extends RuntimeException {
	public NotFoundException(String id) {
		super("Entry:" + id + " was not found!");
	}
}
