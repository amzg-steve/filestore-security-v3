package com.stevesmedia.filestore.restapi.service.security;

import org.springframework.http.ResponseEntity;

import com.stevesmedia.filestore.restapi.domainmodel.security.LoginUser;

public interface AuthService {

	/**
	 * @param tokenReq
	 * @return
	 */
	ResponseEntity<?> performAuthorize(LoginUser tokenReq);

	String revokeToken(String string);

}