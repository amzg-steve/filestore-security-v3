package com.stevesmedia.filestore.restapi.exceptions;

public class FileOperationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public FileOperationException(String message, Throwable cause) {
		super(message, cause);
	}

}
