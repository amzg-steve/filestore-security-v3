package com.stevesmedia.filestore.restapi.domainmodel.security;

import java.io.Serializable;

import lombok.Data;
import lombok.NonNull;

@Data
public class LoginUser implements Serializable{

	private static final long serialVersionUID = 901821181867105280L;
	
	@NonNull
	private String username;
	
	@NonNull
	private String password;

	public LoginUser(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public LoginUser() {
		super();
	}

}
