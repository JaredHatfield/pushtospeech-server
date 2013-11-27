package com.unitvectory.pushtospeech.server;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

/**
 * The base servlet.
 * 
 * @author Jared Hatfield
 * 
 */
public abstract class PushToSpeechResource extends HttpServlet {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = -1051783454359461968L;

    /**
     * The API key.
     */
    private static String apiKey;

    /**
     * Return a JSON status message
     * 
     * @param resp
     *            The servlet response
     * @param code
     *            The response code
     * @param message
     *            The message
     */
    protected void returnJsonStatus(HttpServletResponse resp, int code,
            String message) {
        if (code != 200) {
            resp.setStatus(code);
        }

        String json = "{ }";
        try {
            json = new JSONObject().put("status", message).toString();
        } catch (JSONException e) {
        }

        resp.setContentType("application/json");
        try {
            resp.getWriter().println(json);
        } catch (IOException e) {
        }
    }

    /**
     * Gets the API key.
     * 
     * @return The API key.
     * 
     */
    protected synchronized String getApiKey() {
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
