package com.unitvectory.pushtospeech.server.model;

import java.io.IOException;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;

import com.google.android.gcm.server.Constants;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

/**
 * Helper class for sending push notifications.
 * 
 * @author Jared Hatfield
 * 
 */
public class SendPush {

    /**
     * 
     * @author Jared Hatfield
     * 
     */
    public enum Status {
        /**
         * Success.
         */
        SUCCESS,

        /**
         * Deactivated.
         */
        DEACTIVATED,

        /**
         * Failed.
         */
        FAILED,

        /**
         * Error.
         */
        ERROR,

        /**
         * IO Error.
         */
        IO_ERROR,

        /**
         * Bad config.
         */
        BAD_CONFIG,

        /**
         * Not found.
         */
        NOT_FOUND
    }

    /**
     * Send a push notification.
     * 
     * @param api
     *            The push API key.
     * @param deviceId
     *            The device identifier to look up.
     * @param text
     *            The text to send.
     * @return The status of the request.
     */
    public static Status speak(String api, String deviceId, String text) {
        if (deviceId == null || deviceId.length() == 0) {
            return Status.NOT_FOUND;
        }

        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            Key key =
                    KeyFactory.createKey(PushToken.class.getSimpleName(),
                            deviceId);
            PushToken pushToken = pm.getObjectById(PushToken.class, key);

            if (pushToken == null) {
                return Status.NOT_FOUND;
            } else if (!pushToken.isValid()) {
                return Status.DEACTIVATED;
            } else {
                // Push the message!
                String registartionId = pushToken.getRegistrationId();
                if (api == null) {
                    return Status.BAD_CONFIG;
                }

                Sender sender = new Sender(api);
                Message message =
                        new Message.Builder().addData("speak", text).build();
                try {
                    Result result = sender.sendNoRetry(message, registartionId);
                    if (result == null) {
                        // It failed
                        return Status.FAILED;
                    } else if (result.getMessageId() != null) {
                        // It worked!
                        return Status.SUCCESS;
                    } else {
                        String error = result.getErrorCodeName();
                        if (error.equals(Constants.ERROR_NOT_REGISTERED)) {
                            // application has been removed from device -
                            // unregister it
                            pushToken.setValid(false);
                            return Status.DEACTIVATED;
                        } else {
                            // Something else bad happened
                            return Status.ERROR;
                        }
                    }
                } catch (IOException e) {
                    return Status.IO_ERROR;
                }
            }
        } catch (JDOObjectNotFoundException e) {
            return Status.NOT_FOUND;
        } finally {
            pm.close();
        }
    }
}
