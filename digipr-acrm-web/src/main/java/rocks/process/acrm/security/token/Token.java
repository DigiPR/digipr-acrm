/*
 * Copyright (c) 2018. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package rocks.process.acrm.security.token;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Token {

    @Id
    private String token;

    public Token() {}

    public Token(String token) {
        this.token = token;
    }
}
