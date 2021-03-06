
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Push to Speech</title>
    
    <link href="./static/css/bootstrap.min.css" rel="stylesheet">
    <link href="./static/css/bootstrap-theme.min.css" rel="stylesheet">
    <link href="./static/css/style.css" rel="stylesheet">
  </head>
  <%@ page import="com.unitvectory.pushtospeech.server.model.SendPush " %>
  <body>

    <div class="container">
      <div class="header">
        <h1><img src="./static/img/p2s.png" height="60" width="60" /> Push to Speech for Android</h1>
      </div>
      <% SendPush.Status status = (SendPush.Status)request.getAttribute("status"); %>
      <% 
      if(status != null) { 
          switch (status) {
              case BAD_CONFIG:
                  out.println("<div class=\"alert alert-danger\">Bad configuration file.</div>");
                  break;
              case DEACTIVATED:
                  out.println("<div class=\"alert alert-warning\">Device has been deactivated.</div>");
                  break;
              case ERROR:
                  out.println("<div class=\"alert alert-danger\">Internal error.</div>");
                  break;
              case FAILED:
                  out.println("<div class=\"alert alert-danger\">Internal error.</div>");
                  break;
              case IO_ERROR:
                  out.println("<div class=\"alert alert-danger\">Internal error.</div>");
                  break;
              case NOT_FOUND:
                  out.println("<div class=\"alert alert-warning\">Device not found.</div>");
                  break;
              case SUCCESS:
                  out.println("<div class=\"alert alert-success\">Success!</div>");
                  break;
              default:
                  break;
          }
      } 
      %>
      
      <div class="row jumbotron">
        <div class="col-md-4">
          <h2>Step 1:</h2>
          <p>Install the Push to Speech app on your Android device.</p>
          <a href="https://play.google.com/store/apps/details?id=com.unitvectory.pushtospeech.client"><img src="./static/img/googleplay.png" /></a>
        </div>
        <div class="col-md-4">
          <h2>Step 2:</h2>
          <p>Get your device identifier from the app.</p>
          <img src="./static/img/pushtospeech-client.png" />
        </div>
        <div class="col-md-4">
          <h2>Step 3:</h2>
          <p>Send messages to your device to read aloud.</p>
          <% String urlId = request.getParameter("id"); %>
          <% String deviceId = request.getParameter("deviceId"); %>
          <% String id = (deviceId != null) ? deviceId : urlId; %>
          <form role="form" method="post" action="<% if(id == null) { out.println("/"); } else { out.println("/?id=" + id); } %>">
            <div class="form-group">
              <input type="text" class="form-control" name="deviceId" id="deviceId" <% if(id != null) { out.print("value=\"" + id + "\""); } %> placeholder="Device Identifier" maxlength="32">
            </div>
            <div class="form-group">
              <textarea class="form-control" rows="3" name="speakText" id="speakText" placeholder="Text to read aloud..." maxlength="1000"></textarea>
            </div>
            <button type="submit" class="btn btn-lg btn-success">Speak</button>
          </form>
        </div>
      </div>
      
      <div class="row info">
        <div>
          <h1>REST API</h1>
          <p>
            Push to Speech provides a very simple JSON API to allow programs to send messages to
            Android devices to be read aloud. To use this API send a simple JSON HTTP POST to
            <i>https://pushtospeech.appspot.com/api/v1/speech</i> as documented below.
            The <i>deviceid</i> specifies the device identifier that will read the message specified
            by <i>text</i> aloud.
          </p>
          <h3>Example POST</h3>
<pre class="code">
POST /api/v1/speech
Host: pushtospeech.appspot.com
Content-Type: application/json
{
    "deviceid":"EXAMPLETOKEN",
    "text":"Hello, world!"
} 
</pre>
          <h3>Example Response</h3>
<pre class="code">
{
    "status": "success"
}
</pre>
        </div>
        <div>
          <h1>Open Source on <img src="./static/img/GitHub_Logo.png" width="100" height="41" /></h1>
          <ul>
            <li>Android Application <a href="https://github.com/JaredHatfield/pushtospeech-client">pushtospeech-client</a>
            <li>AppEngine Application <a href="https://github.com/JaredHatfield/pushtospeech-server">pushtospeech-server</a></li>
          </ul>
        </div>
      </div>

      <div class="footer">
        <p>A UnitVectorY Production</p>
      </div>

    </div> <!-- /container -->


    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="./static/js/jquery-1.10.2.min.js"></script>
    <script src="./static/js/bootstrap.min.js"></script>
  </body>
</html>