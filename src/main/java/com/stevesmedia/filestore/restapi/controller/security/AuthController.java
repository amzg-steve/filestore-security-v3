package com.stevesmedia.filestore.restapi.controller.security;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.stevesmedia.filestore.restapi.domainmodel.security.AuthToken;
import com.stevesmedia.filestore.restapi.domainmodel.security.LoginUser;
import com.stevesmedia.filestore.restapi.service.security.AuthService;

@RestController
public class AuthController {
	
	@Autowired
	private AuthService authService;
	
	@PostMapping(value="/authorize")
	public ResponseEntity<?> generateJWT(@RequestBody @Valid LoginUser tokenReq) {
		
    	return authService.performAuthorize(tokenReq);
		
	}
	
	/**
	 * Revoke an existing token
	 * @param token
	 * @return
	 */
	@PostMapping(value="/revokeToken")
    @PreAuthorize("hasRole('ADMIN')")
	public String revokeToken(@RequestBody AuthToken token) {
		
		return authService.revokeToken(token.getToken());
	}

}
