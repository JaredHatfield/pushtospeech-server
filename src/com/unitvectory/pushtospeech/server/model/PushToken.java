package com.unitvectory.pushtospeech.server.model;

import javax.jdo.PersistenceManager;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class PushToken {

    @PrimaryKey
    private String id;

    @Persistent
    private String secret;

    @Persistent
    private String token;

    @Persistent
    private boolean valid;

    public PushToken() {
    }

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

    public static void save(PushToken token) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            pm.makePersistent(token);
        } finally {
            pm.close();
        }
    }
}
