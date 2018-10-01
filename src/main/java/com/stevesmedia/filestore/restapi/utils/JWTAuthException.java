package com.stevesmedia.filestore.restapi.utils;

/**
* JWT authorization exception
*/
public class JWTAuthException extends RuntimeException {

	private static final long serialVersionUID = 5829295709502254020L;

    public JWTAuthException(String message, Throwable cause) {
        super(message, cause);
    }
}
