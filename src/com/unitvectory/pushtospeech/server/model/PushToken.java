package com.unitvectory.pushtospeech.server.model;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * The push token record.
 * 
 * @author Jared Hatfield
 * 
 */
@PersistenceCapable
public class PushToken {

    /**
     * The id
     */
    @PrimaryKey
    private String id;

    /**
     * The secret
     */
    @Persistent
    private String secret;

    /**
     * The token
     */
    @Persistent
    private String token;

    /**
     * The valid flag
     */
    @Persistent
    private boolean valid;

    /**
     * Initializes a new instance of the PushToken class.
     */
    public PushToken() {
    }

    /**
     * Initializes a new instance of the PushToken class.
     * 
     * @param id
     *            The id
     * @param secret
     *            The secret
     * @param token
     *            The token
     */
    public PushToken(String id, String secret, String token) {
        this.id = id;
        this.secret = secret;
        this.token = token;
        this.valid = true;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the secret
     */
    public String getSecret() {
        return secret;
    }

    /**
     * @param secret
     *            the secret to set
     */
    public void setSecret(String secret) {
        this.secret = secret;
    }

    /**
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * @param token
     *            the token to set
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * @return the valid
     */
    public boolean isValid() {
        return valid;
    }

    /**
     * @param valid
     *            the valid to set
     */
    public void setValid(boolean valid) {
        this.valid = valid;
    }
}
