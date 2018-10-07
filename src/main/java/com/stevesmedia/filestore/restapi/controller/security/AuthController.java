package com.stevesmedia.filestore.restapi.controller.security;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.stevesmedia.filestore.restapi.domainmodel.security.AuthToken;
import com.stevesmedia.filestore.restapi.domainmodel.security.TokenAuthRequest;
import com.stevesmedia.filestore.restapi.exceptions.JWTAuthException;
import com.stevesmedia.filestore.restapi.utils.JwtTokenUtils;

@RestController
public class AuthController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtTokenUtils jwtTokenUtils;
    
	@PostMapping(value="/authorize")
	public ResponseEntity<?> generateJWT(@RequestBody @Valid TokenAuthRequest tokenReq) {
		
    	Authentication authentication = null;
        try {
        	authentication = authenticationManager.authenticate(
        			new UsernamePasswordAuthenticationToken(tokenReq.getUsername(), tokenReq.getPassword()));
        } catch (DisabledException exp) {
            throw new JWTAuthException("User is disabled", exp);
        } catch (BadCredentialsException exp) {
            throw new JWTAuthException("Invalid credentials", exp);
        }
        
        final String token = jwtTokenUtils.generateToken(authentication);

        return ResponseEntity.ok(new AuthToken(token));
		
	}

}
