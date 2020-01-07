<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Register</title>
</head>
<body>
<%
    String errorVariable = "";
    if (request.getParameter("userexists") != null) {
        errorVariable = "Istnieje";
    } else if (request.getParameter("baddata") != null) {
        errorVariable = "Zle dane";
    }
%>
<%= errorVariable%>
<form:form action="/register" method="POST" modelAttribute="loginData">
    <div><form:label path="username"> User Name : <form:input path="username" type="text" name="username"/>
    </form:label></div>
    <div><form:label path="password"> Password: <form:input path="password" type="password" name="password"/>
    </form:label></div>
    <div><input type="submit" value="REGISTER"/></div>
</form:form>
</body>
</html>