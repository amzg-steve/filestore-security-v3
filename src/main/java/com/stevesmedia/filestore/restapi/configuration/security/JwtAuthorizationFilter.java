package com.stevesmedia.filestore.restapi.configuration.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.stevesmedia.filestore.restapi.domainmodel.security.Constants;
import com.stevesmedia.filestore.restapi.utils.JwtTokenUtils;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {
	
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

	@Override
	protected void doFilterInternal(HttpServletRequest request, 
										HttpServletResponse response, 
										FilterChain filterChain)
										throws ServletException, IOException {
		
		String header = request.getHeader(Constants.HEADER_STRING);
        String username = null;
        String authToken = null;
        if (header != null && header.startsWith(Constants.TOKEN_PREFIX)) {
            authToken = header.replace(Constants.TOKEN_PREFIX,"");
            try {
                username = jwtTokenUtils.getUsernameFromToken(authToken);
            } catch (IllegalArgumentException e) {
                logger.error("an error occured during getting username from token", e);
            } catch (ExpiredJwtException e) {
                logger.error("The token is expired and not valid anymore", e);
            } catch(SignatureException e){
                logger.error("Authentication Failed. Username or Password not valid.");
                //throw new JWTAuthException("Invalid token", e);
                //response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Invalid token");
                //return;
                
            }
        } else {
            logger.warn("couldn't find bearer token, will ignore the header");
        }
        
        logger.debug("checking authentication for user '{}'", username);
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        	
            logger.debug("security context was null, so authorizating user");

            if (jwtTokenUtils.validateToken(authToken)) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                														username, null, 
                														jwtTokenUtils.getAuthorities(authToken));
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                logger.info("authorizated user '{}', setting security context", username);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
		
		filterChain.doFilter(request, response);

	}

}
