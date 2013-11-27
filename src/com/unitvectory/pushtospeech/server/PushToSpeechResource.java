package com.unitvectory.pushtospeech.server;

import java.io.InputStream;
import java.util.Properties;

import javax.servlet.http.HttpServlet;

/**
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
