package com.unitvectory.pushtospeech.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.unitvectory.pushtospeech.server.model.SendPush;

/**
 * The home page servlet.
 * 
 * @author Jared Hatfield
 * 
 */
public class HomeResource extends PushToSpeechResource {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 5908336374635835136L;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse resp)
            throws IOException, ServletException {
        request.getRequestDispatcher("/home.jsp").forward(request, resp);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse resp)
            throws ServletException, IOException {
        String deviceId = request.getParameter("deviceId");
        String speakText = request.getParameter("speakText");
        SendPush.Status status =
                SendPush.speak(this.getApiKey(), deviceId, speakText);
        request.setAttribute("status", status);
        request.getRequestDispatcher("/home.jsp").forward(request, resp);
    }
}
