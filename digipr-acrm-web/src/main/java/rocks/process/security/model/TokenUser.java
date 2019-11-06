/*
 * Copyright (c) 2019. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package rocks.process.security.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.security.core.userdetails.User;

import static java.util.Collections.emptyList;

public class TokenUser extends User {
    private String email;
    private String remember;

    @JsonCreator
    public TokenUser(@JsonProperty(value = "email", required = true) String email, @JsonProperty(value = "password", required = true) String password, @JsonProperty(value = "remember", required = true) String remember){
        super(email, password, emptyList());
        this.email = email;
        this.remember = remember;
    }

    public String getEmail() {
        return email;
    }

    public String getRemember() {
        return remember;
    }

}
