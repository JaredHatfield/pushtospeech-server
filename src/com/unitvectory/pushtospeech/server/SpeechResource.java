package com.unitvectory.pushtospeech.server;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.unitvectory.pushtospeech.server.model.SendPush;

/**
 * 
 * @author Jared Hatfield
 * 
 */
public class SpeechResource extends PushToSpeechResource {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = -3522780854690776001L;

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
        SendPush.Status status = SendPush.speak(this.getApiKey(), id, text);
        switch (status) {
            case BAD_CONFIG:
                resp.setStatus(500);
                resp.setContentType("application/json");
                resp.getWriter().println("{ \"status\":\"bad config\" }");
                break;
            case DEACTIVATED:
                resp.setStatus(404);
                resp.setContentType("application/json");
                resp.getWriter().println("{ \"status\":\"deactivated\" }");
                break;
            case ERROR:
                resp.setStatus(500);
                resp.setContentType("application/json");
                resp.getWriter().println("{ \"status\":\"error\" }");
                break;
            case FAILED:
                resp.setStatus(500);
                resp.setContentType("application/json");
                resp.getWriter().println("{ \"status\":\"failed\" }");
                break;
            case IO_ERROR:
                resp.setStatus(500);
                resp.setContentType("application/json");
                resp.getWriter().println("{ \"status\":\"io error\" }");
                break;
            case NOT_FOUND:
                resp.setStatus(404);
                resp.setContentType("application/json");
                resp.getWriter().println("{ \"status\":\"not found\" }");
                break;
            case SUCCESS:
                resp.setContentType("application/json");
                resp.getWriter().println("{ \"status\":\"success\" }");
                break;
            default:
                break;
        }
    }
}
