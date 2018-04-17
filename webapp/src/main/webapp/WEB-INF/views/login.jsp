<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Authentication</title>
</head>
<body>
<h3>${msg}</h3>
<form action="login" method="post">       
        <p>
            <label for="username"></label>
            <input type="text" id="username" name="username"/>  
        </p>
        <p>
            <label for="password"></label>
            <input type="password" id="password" name="password"/>  
        </p>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <button type="submit" class="btn btn-primary"> Log In </button>
</form>
</body>
</html>