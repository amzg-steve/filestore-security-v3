package com.stevesmedia.filestore.restapi.configuration.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.stevesmedia.filestore.restapi.service.security.JwtUserDetailsProviderService;
import com.stevesmedia.filestore.restapi.utils.JwtTokenUtils;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {
	
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private JwtUserDetailsProviderService userDetailsService;
    
    @Autowired
    private JwtTokenUtils jwtTokenUtils;
    
    public JwtAuthorizationFilter(JwtUserDetailsProviderService userDetailsService, JwtTokenUtils jwtTokenUtil) {
    	this.userDetailsService = userDetailsService;
    	this.jwtTokenUtils = jwtTokenUtil;
    	
    }

	@Override
	protected void doFilterInternal(HttpServletRequest request, 
										HttpServletResponse response, 
										FilterChain filterChain)
										throws ServletException, IOException {
		
		filterChain.doFilter(request, response);

	}

}
