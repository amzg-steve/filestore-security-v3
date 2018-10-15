package com.stevesmedia.filestore.restapi.exceptions;

public class ResourceNotFound extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ResourceNotFound(String message, Throwable cause) {
		super(message, cause);
	}

}
