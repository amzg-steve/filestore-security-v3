package com.stevesmedia.filestore.restapi.controller.security;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.stevesmedia.filestore.restapi.domainmodel.Response;
import com.stevesmedia.filestore.restapi.domainmodel.security.LoginUser;
import com.stevesmedia.filestore.restapi.service.security.AuthService;

/**
 * Central controller for handling user authorization
 * @author us-photon
 *
 */
@RestController
public class AuthController {
	
	private AuthService authService;

	@Autowired
	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping(value="/authorize")
	public ResponseEntity<?> generateJWT(@RequestBody @Valid LoginUser tokenReq) {
		
    	return authService.performAuthorize(tokenReq);
		
	}
	
	/**
	 * Revoke existing token for a user 
	 * @param token
	 * @return
	 */
	@DeleteMapping(value="/revokeToken/user/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
	public Response<String> revokeToken(@PathVariable  String userId) {
		
		return authService.deleteToken(userId);
	}

}
