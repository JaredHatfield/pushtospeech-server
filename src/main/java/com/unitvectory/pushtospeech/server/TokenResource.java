package com.unitvectory.pushtospeech.server;

import java.io.BufferedReader;
import java.io.IOException;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.unitvectory.pushtospeech.server.model.PMF;
import com.unitvectory.pushtospeech.server.model.PushToken;

/**
 * API to manage push tokens
 * 
 * @author Jared Hatfield
 * 
 */
public class TokenResource extends PushToSpeechResource {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = -6518160436759694299L;

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse resp)
            throws ServletException, IOException {

        StringBuffer jb = new StringBuffer();
        String line = null;
        try {
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null)
                jb.append(line);
        } catch (Exception e) {
            this.returnJsonStatus(resp, 400, "invalid request");
            return;
        }

        // Get the content from the json
        String deviceId;
        String deviceSecret;
        String registrationId;
        try {
            JSONObject object = new JSONObject(jb.toString());
            deviceId = object.getString("deviceid");
            deviceSecret = object.getString("devicesecret");
            registrationId = object.getString("registrationid");
        } catch (JSONException e) {
            this.returnJsonStatus(resp, 400, "invalid json");
            return;
        }

        // Verify the id, secret, and token
        if (deviceId == null || deviceId.isEmpty() || deviceSecret == null
                || deviceSecret.isEmpty() || registrationId == null
                || registrationId.isEmpty()) {
            this.returnJsonStatus(resp, 400, "bad request");
            return;
        }

        // Create or update the push token
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            Key key =
                    KeyFactory.createKey(PushToken.class.getSimpleName(),
                            deviceId);
            PushToken pushToken = pm.getObjectById(PushToken.class, key);
            if (deviceSecret != null
                    && deviceSecret.equals(pushToken.getDeviceSecret())) {
                pushToken.setRegistrationId(registrationId);
                this.returnJsonStatus(resp, 200, "updated");
            } else {
                this.returnJsonStatus(resp, 401, "unauthorized");
            }
        } catch (JDOObjectNotFoundException e) {
            PushToken newToken =
                    new PushToken(deviceId, deviceSecret, registrationId);
            pm.makePersistent(newToken);
            this.returnJsonStatus(resp, 200, "created");
        } finally {
            pm.close();
        }
    }
}
