package com.unitvectory.pushtospeech.server;

import java.io.BufferedReader;
import java.io.IOException;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
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
public class TokenResource extends HttpServlet {

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
            resp.setStatus(400);
            resp.setContentType("application/json");
            resp.getWriter().println("{ \"status\":\"invalid request\" }");
            return;
        }

        // Get the content from the json
        String id;
        String secret;
        String token;
        try {
            JSONObject object = new JSONObject(jb.toString());
            id = object.getString("id");
            secret = object.getString("secret");
            token = object.getString("token");
        } catch (JSONException e) {
            resp.setStatus(400);
            resp.setContentType("application/json");
            resp.getWriter().println("{ \"status\":\"invalid json\" }");
            return;
        }

        // TODO: Verify the id, secret, and token

        // Create or update the push token
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            Key key = KeyFactory.createKey(PushToken.class.getSimpleName(), id);
            PushToken pushToken = pm.getObjectById(PushToken.class, key);
            if (secret != null && secret.equals(pushToken.getSecret())) {
                pushToken.setToken(token);

                resp.setContentType("application/json");
                resp.getWriter().println("{ \"status\":\"updated\" }");
            } else {
                resp.setStatus(401);
                resp.setContentType("application/json");
                resp.getWriter().println("{ \"status\":\"unauthorized\" }");
            }
        } catch (JDOObjectNotFoundException e) {
            PushToken newToken = new PushToken(id, secret, token);
            PushToken.save(newToken);

            resp.setContentType("application/json");
            resp.getWriter().println("{ \"status\":\"created\" }");
        } finally {
            pm.close();
        }
    }
}
