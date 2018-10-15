package com.stevesmedia.filestore.restapi.domainmodel.security;

public class Constants {
	
    public static final Long ACCESS_TOKEN_VALIDITY_SECONDS = (long)(10*60);
    public static final String SIGNING_KEY = "stevesmedia";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String AUTHORITIES_KEY = "scopes";
    public static final String TOKEN_ISSUER = "stevesmedia";


}
