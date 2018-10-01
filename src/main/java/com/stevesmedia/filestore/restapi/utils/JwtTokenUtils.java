package com.stevesmedia.filestore.restapi.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.stevesmedia.filestore.restapi.domainmodel.security.Constants;

import io.jsonwebtoken.Clock;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClock;

@Component
public class JwtTokenUtils {
	
    private Clock clock = DefaultClock.INSTANCE;

	public String generateToken(UserDetails userDetails) {
		
        final Date createdDate = clock.now();
        String token =  null;
        final Date expirationDate = calculateExpirationDate(createdDate);

        token = Jwts.builder()
        		.setSubject(userDetails.getUsername())
        		.setExpiration(expirationDate)
        		.claim(Constants.AUTHORITIES_KEY, userDetails.getAuthorities())
        		.setIssuedAt(createdDate)
        		.setIssuer(Constants.TOKEN_ISSUER)
        		.signWith(SignatureAlgorithm.HS512, Constants.SIGNING_KEY)
        		.compact();
        
		return token;
	}


	private Date calculateExpirationDate(Date createdDate) {
    	Date expDate = new Date(createdDate.getTime() + Constants.ACCESS_TOKEN_VALIDITY_SECONDS * 1000);
    	SimpleDateFormat formatter = new SimpleDateFormat("mm-dd-yyyy hh:mm:ss");  
        System.out.println("Date Format: "+formatter.format(expDate));  
        return expDate;
	}

}
