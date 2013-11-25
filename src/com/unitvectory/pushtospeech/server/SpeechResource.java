package com.unitvectory.pushtospeech.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.android.gcm.server.Constants;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.unitvectory.pushtospeech.server.model.PMF;
import com.unitvectory.pushtospeech.server.model.PushToken;

/**
 * 
 * @author Jared Hatfield
 * 
 */
public class SpeechResource extends HttpServlet {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = -3522780854690776001L;

    /**
     * The API key.
     */
    private static String apiKey;

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
        String text;
        try {
            JSONObject object = new JSONObject(jb.toString());
            id = object.getString("id");
            text = object.getString("text");
        } catch (JSONException e) {
            resp.setStatus(400);
            resp.setContentType("application/json");
            resp.getWriter().println("{ \"status\":\"invalid json\" }");
            return;
        }

        // TODO: Verify the id, text

        // Create or update the push token
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            Key key = KeyFactory.createKey(PushToken.class.getSimpleName(), id);
            PushToken pushToken = pm.getObjectById(PushToken.class, key);

            if (pushToken == null || !pushToken.isValid()) {

            } else {
                // Push the message!
                String registartionId = pushToken.getToken();
                String myKey = this.getApiKey();
                if (myKey == null) {
                    resp.setStatus(500);
                    resp.setContentType("application/json");
                    resp.getWriter().println("{ \"status\":\"bad config\" }");
                }

                Sender sender = new Sender(myKey);
                Message message =
                        new Message.Builder().addData("speak", text).build();
                try {
                    Result result = sender.sendNoRetry(message, registartionId);
                    if (result == null) {
                        // It failed
                        resp.setStatus(500);
                        resp.setContentType("application/json");
                        resp.getWriter().println("{ \"status\":\"failed\" }");
                    } else if (result.getMessageId() != null) {
                        // It worked!
                        resp.setContentType("application/json");
                        resp.getWriter().println("{ \"status\":\"success\" }");
                    } else {
                        String error = result.getErrorCodeName();
                        if (error.equals(Constants.ERROR_NOT_REGISTERED)) {
                            // application has been removed from device -
                            // unregister it
                            pushToken.setValid(false);
                            resp.setStatus(404);
                            resp.setContentType("application/json");
                            resp.getWriter().println(
                                    "{ \"status\":\"deactivated\" }");
                        } else {
                            // Something else bad happened
                            resp.setStatus(500);
                            resp.setContentType("application/json");
                            resp.getWriter()
                                    .println("{ \"status\":\"error\" }");
                        }
                    }
                } catch (IOException e) {
                    resp.setStatus(500);
                    resp.setContentType("application/json");
                    resp.getWriter().println("{ \"status\":\"io error\" }");
                }
            }
        } catch (JDOObjectNotFoundException e) {
            resp.setStatus(404);
            resp.setContentType("application/json");
            resp.getWriter().println("{ \"status\":\"not found\" }");
        } finally {
            pm.close();
        }
    }

    /**
     * Gets the API key.
     * 
     * @return The API key.
     * 
     */
    private synchronized String getApiKey() {
        if (apiKey == null) {
            try {
                // Load the key from the config
                Properties property = new Properties();
                InputStream is =
                        this.getServletContext().getResourceAsStream(
                                "/WEB-INF/app.properties");
                property.load(is);
                apiKey = property.getProperty("api");
            } catch (Exception e) {
                // Failed to load.
            }
        }

        return apiKey;
    }
}
