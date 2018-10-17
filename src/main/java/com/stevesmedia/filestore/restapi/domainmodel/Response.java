package com.stevesmedia.filestore.restapi.domainmodel;

import java.util.Date;
import java.util.List;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class Response<T> extends Resources<T> {
	
	private Date timeStamp;
	private boolean status;
	private HttpStatus httpStatus;
	private List<String> message;
	private Resource<T> resultData;
	
	public Response() {
		
	}

	public Response(Date timeStamp, boolean status, HttpStatus httpStatus, List<String> message, Resource<T> result) {
		this.timeStamp = timeStamp;
		this.status = status;
		this.httpStatus = httpStatus;
		this.message = message;
		this.resultData = result;
	}

	public Response(List<String> message, boolean status, Date timeStamp, HttpStatus httpStatus) {
		this.message = message;
		this.status = status;
		this.timeStamp = timeStamp;
		this.httpStatus = httpStatus;
	}


}
