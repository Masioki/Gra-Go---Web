<!DOCTYPE html>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
    <title>Spring Security Example </title>
</head>
<body>
<%
    String errorVariable = "";
    if (request.getParameter("error") != null) {
        errorVariable = "Blad";
    } else if (request.getParameter("logout") != null) {
        errorVariable = "Wylogowano";
    }
%>
<%= errorVariable%>
<form:form action="/login" method="POST" modelAttribute="loginData">
    <div><form:label path="username"> User Name : <form:input path="username" type="text" name="username"/>
    </form:label></div>
    <div><form:label path="password"> Password: <form:input path="password" type="password" name="password"/>
    </form:label></div>
    <div><input type="submit" value="Sign In"/></div>
</form:form>
<button type="submit" onclick="location.href='/register'" value="register">Register</button>
</body>
</html>