package com.stevesmedia.filestore.restapi.exceptions;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.stevesmedia.filestore.restapi.domainmodel.ExceptionResponse;

/**
 * General exception handler for controller actions goes here
 * @author us-photon
 *
 */
@RestControllerAdvice
@RestController
public class CustomRespEntityExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(Exception.class)
	public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) throws Exception {
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), 500, 
				HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<Object>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);

	}
	
	@ExceptionHandler(ResourceNotFound.class)
	public final ResponseEntity<Object> handleFilNotFoundException(ResourceNotFound ex, WebRequest request) throws Exception {
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), 404, 
				HttpStatus.NOT_FOUND, ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<Object>(exceptionResponse, HttpStatus.NOT_FOUND);

	}
	
	@ExceptionHandler(JWTAuthException.class)
	public final ResponseEntity<Object> handleJwtAuthException(JWTAuthException ex, WebRequest request) throws Exception {
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), 401, 
				HttpStatus.UNAUTHORIZED, ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<Object>(exceptionResponse, HttpStatus.UNAUTHORIZED);

	}

}
