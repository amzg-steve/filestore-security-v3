package com.stevesmedia.filestore.restapi.domainmodel.security;

import java.io.Serializable;

import lombok.Data;
import lombok.NonNull;

@Data
public class TokenAuthRequest implements Serializable{

	private static final long serialVersionUID = 901821181867105280L;
	
	@NonNull
	private String username;
	
	@NonNull
	private String password;

	public TokenAuthRequest(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public TokenAuthRequest() {
		super();
	}

}
