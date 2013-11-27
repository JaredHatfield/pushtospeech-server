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
            this.returnJsonStatus(resp, 400, "invalid request");
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
            this.returnJsonStatus(resp, 400, "invalid json");
            return;
        }

        // Verify the id, text
        if (id == null || id.isEmpty() || text == null || text.isEmpty()) {
            this.returnJsonStatus(resp, 500, "bad request");
            return;
        }

        // Create or update the push token
        SendPush.Status status = SendPush.speak(this.getApiKey(), id, text);
        switch (status) {
            case BAD_CONFIG:
                this.returnJsonStatus(resp, 500, "bad config");
                break;
            case DEACTIVATED:
                this.returnJsonStatus(resp, 404, "deactivated");
                break;
            case ERROR:
                this.returnJsonStatus(resp, 500, "error");
                break;
            case FAILED:
                this.returnJsonStatus(resp, 500, "failed");
                break;
            case IO_ERROR:
                this.returnJsonStatus(resp, 500, "io error");
                break;
            case NOT_FOUND:
                this.returnJsonStatus(resp, 404, "not found");
                break;
            case SUCCESS:
                this.returnJsonStatus(resp, 200, "success");
                break;
            default:
                break;
        }
    }
}
