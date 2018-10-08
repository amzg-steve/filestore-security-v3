package com.stevesmedia.filestore.restapi.service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.stevesmedia.filestore.restapi.domainmodel.security.AuthToken;
import com.stevesmedia.filestore.restapi.domainmodel.security.LoginUser;
import com.stevesmedia.filestore.restapi.exceptions.JWTAuthException;
import com.stevesmedia.filestore.restapi.utils.JwtTokenUtils;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class AuthServiceImpl implements AuthService {

	@Autowired
	public AuthenticationManager authenticationManager;

	@Autowired
	public JwtTokenUtils jwtTokenUtils;

	/**
	 * Authorize user, generate jwt token and store token in the ehCache
	 * 
	 * @see com.stevesmedia.filestore.restapi.service.security.AuthService#performAuthorize(com.stevesmedia.filestore.restapi.domainmodel.security.LoginUser)
	 */
	@Override
	public ResponseEntity<?> performAuthorize(LoginUser tokenReq) {

		Authentication authentication = null;
		try {
			authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					tokenReq.getUsername(), tokenReq.getPassword()));
		} catch (DisabledException exp) {
			throw new JWTAuthException("User is disabled", exp);
		} catch (BadCredentialsException exp) {
			throw new JWTAuthException("Invalid credentials", exp);
		}

		String token = jwtTokenUtils.generateToken(authentication);
		
		//handle revocation of tokens scenario
		jwtTokenUtils.storeTokenInCache(token);
		log.info("Token stored in cache");

		return ResponseEntity.ok(new AuthToken(token));
	}

	@Override
	public String revokeToken(String token) {
		
		boolean sucess = false;
		sucess = jwtTokenUtils.removeTokenFromCache(token);
		if (sucess) {
			return "Token is removed";
		}
		return "Token removal failed";
	}

}