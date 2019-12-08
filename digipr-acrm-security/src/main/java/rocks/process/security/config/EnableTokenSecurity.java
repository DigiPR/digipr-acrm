/*
 * Copyright (c) 2019. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package rocks.process.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import rocks.process.security.service.TokenService;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import({TokenSecurityConfiguration.class,
        TokenSecurityProperties.class,
        TokenService.class})
@Configuration
public @interface EnableTokenSecurity {
}
