package com.stevesmedia.filestore.restapi.service.security;

import java.util.Collections;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.stevesmedia.filestore.restapi.domainmodel.Response;
import com.stevesmedia.filestore.restapi.domainmodel.security.AuthToken;
import com.stevesmedia.filestore.restapi.domainmodel.security.LoginUser;
import com.stevesmedia.filestore.restapi.exceptions.ResourceNotFound;
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
	 * Authorize user, generate jwt token and manage tokens through ehCache
	 * 
	 * @see com.stevesmedia.filestore.restapi.service.security.AuthService#performAuthorize(com.stevesmedia.filestore.restapi.domainmodel.security.LoginUser)
	 */
	@Override
	public ResponseEntity<?> performAuthorize(LoginUser tokenReq) {
		
		String token = null;
		token = JwtTokenUtils.fetchTokenFromCache(tokenReq.getUsername());
		
		if(token != null) {
			//Valid token available on cache, so no need to generate new token
			return ResponseEntity.ok(new AuthToken(token)); 
		}
		
		Authentication authentication = null;
		try {
			authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					tokenReq.getUsername(), tokenReq.getPassword()));
		} catch (DisabledException exp) {
			throw new JWTAuthException("User is disabled", exp);
		} catch (BadCredentialsException exp) {
			throw new JWTAuthException("Invalid credentials", exp);
		}

		token = jwtTokenUtils.generateToken(authentication);
		
		//handle revocation of tokens scenario
		JwtTokenUtils.storeTokenInCache(tokenReq.getUsername(), token);
		log.info("New token added to cache");

		return ResponseEntity.ok(new AuthToken(token));
	}

	@Override
	public Response<String> deleteToken(String userId) {
		
		boolean sucess = false;
		sucess = JwtTokenUtils.deleteTokenFromCache(userId);
		if(!sucess) {
			throw new ResourceNotFound("Token not found", null);
		}
		log.info("Token removed from cache for user: " +userId);
		return new Response<String>(Collections.singletonList("Token is removed"), true, new Date(), HttpStatus.OK);
	}

}