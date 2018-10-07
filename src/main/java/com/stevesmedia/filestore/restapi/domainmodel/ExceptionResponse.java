package com.stevesmedia.filestore.restapi.domainmodel;

import java.util.Date;

import org.springframework.http.HttpStatus;

import lombok.Data;

/**
 * @author us-photon
 *
 */
@Data
public class ExceptionResponse {
	
	private Date timeStamp;
	private int status;
	private HttpStatus error;
	private String message;
	private String reqDetails;
	
	public ExceptionResponse(Date timeStamp, int status, HttpStatus error, String message, String reqDetails) {
		this.timeStamp = timeStamp;
		this.status = status;
		this.error = error;
		this.message = message;
		this.reqDetails = reqDetails;
	}

	public ExceptionResponse() {
		
	}
	
	

}
