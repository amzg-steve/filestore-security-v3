package com.stevesmedia.filestore.restapi.service.security;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;

import com.stevesmedia.filestore.restapi.domainmodel.Response;
import com.stevesmedia.filestore.restapi.domainmodel.security.LoginUser;

public interface AuthService {

	/**
	 * @param tokenReq
	 * @return
	 */
	ResponseEntity<?> performAuthorize(LoginUser tokenReq);

	Response<String> deleteToken(@Valid String userId);

}