/*
 * Copyright (c) 2018. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package rocks.process.acrm.security;

public class SecurityConstants {
    public static final String COOKIE_TYPE = "cookie";
    public static final String BEARER_TYPE = "bearer";
    public static final long REMEMBER_EXPIRATION_TIME =  864_000_000; // 10 days
    public static final long SESSION_EXPIRATION_TIME =  86_400_000; // 1 day
    public static final long BEARER_EXPIRATION_TIME =  864_000_000; // 10 days
    public static final String BEARER_TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_NAME = "Authorization";
    public static final String COOKIE_NAME = "AUTHORISATION";
}
