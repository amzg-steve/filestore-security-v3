package com.stevesmedia.filestore.restapi.controller.security;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.stevesmedia.filestore.restapi.domainmodel.security.AuthToken;
import com.stevesmedia.filestore.restapi.domainmodel.security.TokenAuthRequest;
import com.stevesmedia.filestore.restapi.service.security.JwtUserDetailsProviderService;
import com.stevesmedia.filestore.restapi.utils.JWTAuthException;
import com.stevesmedia.filestore.restapi.utils.JwtTokenUtils;

@RestController
public class AuthController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtTokenUtils jwtTokenUtils;

	@Autowired
	private JwtUserDetailsProviderService jwtUserDetailsProviderService;

    @ExceptionHandler({JWTAuthException.class})
    public ResponseEntity<String> handleAuthenticationException(JWTAuthException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }
    
	@PostMapping(value="/authorize")
	public ResponseEntity<?> generateJWT(@RequestBody @Valid TokenAuthRequest tokenReq) {
		
        try {
        	authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
        			tokenReq.getUsername(), tokenReq.getPassword()));
        } catch (DisabledException exp) {
            throw new JWTAuthException("User is disabled!", exp);
        } catch (BadCredentialsException exp) {
            throw new JWTAuthException("Bad credentials!", exp);
        }
        
        final UserDetails userDetails = jwtUserDetailsProviderService.loadUserByUsername(tokenReq.getUsername());
        final String token = jwtTokenUtils.generateToken(userDetails);

        return ResponseEntity.ok(new AuthToken(token));
		
	}

}
