
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
        <h3 class="text-muted">Push to Speech for Android</h3>
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
          <p><a class="btn btn-lg btn-success" href="#" role="button">Download App</a></p>
        </div>
        <div class="col-md-4">
          <h2>Step 2:</h2>
        </div>
        <div class="col-md-4">
          <h2>Step 3:</h2>
          <form role="form" method="post">
            <div class="form-group">
              <input type="text" class="form-control" name="deviceId" id="deviceId" placeholder="Device Identifier" maxlength="32">
            </div>
            <div class="form-group">
              <textarea class="form-control" rows="3" name="speakText" id="speakText" placeholder="Speech Text..." maxlength="1000"></textarea>
            </div>
            <button type="submit" class="btn btn-lg btn-success">Speak</button>
          </form>
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