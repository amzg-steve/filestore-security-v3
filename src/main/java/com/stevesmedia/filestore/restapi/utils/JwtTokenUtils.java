package com.stevesmedia.filestore.restapi.utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.stevesmedia.filestore.restapi.domainmodel.security.Constants;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Clock;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClock;

@Component
public class JwtTokenUtils {
	
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private Clock clock = DefaultClock.INSTANCE;

    public String generateToken(Authentication auth) {
		
        final Date createdDate = clock.now();
        String token =  null;
        final Date expirationDate = calculateExpirationDate(createdDate);
        Claims claims = Jwts.claims().setSubject(auth.getName());
        claims.put(Constants.AUTHORITIES_KEY, auth.getAuthorities().stream().map(s -> s.toString()).collect(Collectors.toList()));


        token = Jwts.builder()
        		.setClaims(claims)
        		.setIssuedAt(createdDate)
        		.setIssuer(Constants.TOKEN_ISSUER)
        		.setExpiration(expirationDate)
        		.signWith(SignatureAlgorithm.HS512, Constants.SIGNING_KEY)
        		.compact();
        
		return token;
	}
	
    public Boolean validateToken(String token) {
        return (!isTokenExpired(token));
    }

	private Date calculateExpirationDate(Date createdDate) {
    	Date expDate = new Date(createdDate.getTime() + Constants.ACCESS_TOKEN_VALIDITY_SECONDS * 1000);
    	SimpleDateFormat formatter = new SimpleDateFormat("mm-dd-yyyy hh:mm:ss");  
        logger.info("Date Format: " +formatter.format(expDate));
        return expDate;
	}


	public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);		
	}
	
    public Date getIssuedAtDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getIssuedAt);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }
    
    @SuppressWarnings("unchecked")
	public Collection<? extends GrantedAuthority> getAuthorities(String token) throws JsonParseException, JsonMappingException, IOException {

        final  Claims claims = getAllClaimsFromToken(token);

        List<String> list =  claims.get(Constants.AUTHORITIES_KEY, List.class);
    	final Collection<? extends GrantedAuthority> authorities = list
    			.stream()
                .map(authority -> new SimpleGrantedAuthority(authority))
                .collect(Collectors.toList());
		return authorities;
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
        		.setSigningKey(Constants.SIGNING_KEY)
        		.parseClaimsJws(token)
        		.getBody();
    }
    
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

}
