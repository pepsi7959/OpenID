<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	<title>ทดสอบ OpenID และ OAuth ด้วยภาษา JSP/Servlet 3.0</title>
	  <style type="text/css">
		  * {
			font-family: verdana,sans-serif;
		  }
		  body {
			width: 50em;
			margin: 1em;
		  }
		  div {
			padding: .5em;
		  }
		  table {
			margin: none;
			padding: none;
		  }
		  .alert {
			border: 1px solid #e7dc2b;
			background: #fff888;
		  }
		  .success {
			border: 1px solid #669966;
			background: #88ff88;
		  }
		  .error {
			border: 1px solid #ff0000;
			background: #ffaaaa;
		  }
		  #verify-form {
			border: 1px solid #777777;
			background: #dddddd;
			margin-top: 1em;
			padding-bottom: 0em;
		  }
	  </style>
  </head>
  <body>
    <h1></h1>
    <p>
		หน้าทดสอบการทำงานของ OpenID และ OAuth
    </p>

    <div id="verify-form">
		<ul>
			<li>ทดสอบการทำงานของ OpenID <a href="<%= request.getContextPath()%>/SSOLogin" target="_blank">SSOLogin</a></li>
			<li>ทดสอบการทำงานของ OAuth <a href="<%= request.getContextPath()%>/OAuthConsumer" target="_blank">OAuthConsumer</a></li>
		</ul>
    </div>
  </body>
</html>