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
     * The device id
     */
    @PrimaryKey
    private String deviceId;

    /**
     * The device secret
     */
    @Persistent
    private String deviceSecret;

    /**
     * The registration id
     */
    @Persistent
    private String registrationId;

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
     * @param deviceId
     *            The device id
     * @param deviceSecret
     *            The device secret
     * @param registrationId
     *            The registrationId
     */
    public PushToken(String deviceId, String deviceSecret, String registrationId) {
        this.deviceId = deviceId;
        this.deviceSecret = deviceSecret;
        this.registrationId = registrationId;
        this.valid = true;
    }

    /**
     * @return the deviceId
     */
    public String getDeviceId() {
        return deviceId;
    }

    /**
     * @param deviceId
     *            the deviceId to set
     */
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    /**
     * @return the deviceSecret
     */
    public String getDeviceSecret() {
        return deviceSecret;
    }

    /**
     * @param deviceSecret
     *            the deviceSecret to set
     */
    public void setDeviceSecret(String deviceSecret) {
        this.deviceSecret = deviceSecret;
    }

    /**
     * @return the registrationId
     */
    public String getRegistrationId() {
        return registrationId;
    }

    /**
     * @param registrationId
     *            the registrationId to set
     */
    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
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
