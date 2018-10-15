package com.stevesmedia.filestore.restapi.utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

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
import lombok.extern.log4j.Log4j2;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

@Component
@Log4j2
public class JwtTokenUtils {

	private static final Cache jwtTokensCache = CacheManager.getInstance().getCache("jwtTokenCache");

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

	public Boolean validateToken(String token, String userName) {
		return (!isTokenExpired(token) && cacheHasToken(userName));
	}

	private Date calculateExpirationDate(Date createdDate) {
		Date expDate = new Date(createdDate.getTime() + Constants.ACCESS_TOKEN_VALIDITY_SECONDS * 1000);
		SimpleDateFormat formatter = new SimpleDateFormat("mm-dd-yyyy hh:mm:ss");  
		log.info("Date Format: " +formatter.format(expDate));
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


	public static void storeTokenInCache(String reqUser, String token) {

		jwtTokensCache.put(new Element(reqUser, token));
	}

	public static String fetchTokenFromCache(String reqUser) {

		String token = null;
		jwtTokensCache.evictExpiredElements();
		
		if(jwtTokensCache.get(reqUser) != null) {
			token = (String) jwtTokensCache.get(reqUser).getObjectValue();
		}
		log.info("Token retrieved: " +token);

		return token;
	}

	public static boolean cacheHasToken(String userName) {

		log.info("Tokens b4: " +jwtTokensCache.getSize() +"\n" +jwtTokensCache.getKeysNoDuplicateCheck());

		//clearing out expired tokens
		jwtTokensCache.evictExpiredElements();
		log.info("Tokens After: " +jwtTokensCache.getSize() +"\n" +jwtTokensCache.getKeysNoDuplicateCheck());

		return jwtTokensCache.get(userName) != null;
	}

	public static boolean deleteTokenFromCache(String userId) {

		return jwtTokensCache.remove(userId);
	}

}
