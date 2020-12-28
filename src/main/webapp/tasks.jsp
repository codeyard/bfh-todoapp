<%--
  Created by IntelliJ IDEA.
  User: chleu
  Date: 28.12.2020
  Time: 10:49
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
    <title>Login successful</title>

</head>
<body>
    <h1>Hallo</h1>
    <h1>${user.getUserName()}</h1>
    <c:if test="${user.getTasks().size() == 0}">
        <h2>please add a task</h2>
    </c:if>
    <c:forEach var = "task" items="${user.getTasks()}" >
    Item <c:out value = "${task.getTitle()}"/><p></c:forEach>
</body>
</html>
