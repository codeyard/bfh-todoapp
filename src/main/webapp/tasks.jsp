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
    <h1>Hi ${user.getUserName()}</h1>

    <c:if test="${user.getTasks().size() == 0}">
        <h2>Let's Start by adding a Task!</h2>
    </c:if>

    <button href="/task">Add new Task</button>

    <c:if test="${user.getTasks().size() != 0}">
        <table>
            <tr>
                <th>Title</th>
                <th>Category</th>
                <th>Due Date</th>
                <th>Reserve</th>
            </tr>
        <c:forEach var = "task" items="${user.getTasks()}" >
            <tr>
                <td><c:out value = "${task.getTitle()}"/></td>
                <td><c:out value = "${task.getCategory()}"/></td>
                <td><c:out value = "${task.getDueDate()}"/></td>
                <td><button href="/task/${task.getTaskID()}">Edit</button></td>
            </tr>
        </c:forEach>

        </table>
    </c:if>
</body>
</html>
