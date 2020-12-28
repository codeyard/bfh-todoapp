<%--
  Created by IntelliJ IDEA.
  User: raphaelgerber
  Date: 28.12.20
  Time: 13:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
    <title>Task</title>
</head>
<body>
    <h1>Task: ${task.getTitle()}</h1>

    <form action="task" method="post">
        <input type="hidden" name="taskID" value="${task.getTaskID()}">
        <input type="hidden" name="isNew" value="${isNew}">

        <div>
            <label for="title">Title:</label>
            <input id="title" name="title" type="text" placeholder="title" value="${task.getTitle()}">
        </div>

        <div>
            <label for="category">Category:</label>
            <input id="category" name="category" type="text" placeholder="category" value="${task.getCategory()}">
        </div>

        <div>
            <label for="dueDate">Due Date:</label>
            <input id="dueDate" name="dueDate" type="date" placeholder="dueDate" value="${task.getDueDate()}">
        </div>

        <div>
            <label for="isImportant">Important?</label>
            <input id="isImportant" name="isImportant" type="checkbox" value="true" <c:if test="${task.isImportant()==true}">checked</c:if>>
        </div>

        <c:if test="${isNew==false}">
            <div>
                <label for="isCompleted">Completed?</label>
                <input id="isCompleted" name="isCompleted" type="checkbox" value="true" <c:if test="${task.isCompleted()==true}">checked</c:if>>
            </div>
        </c:if>

        <div>
            <c:choose>
                <c:when test="${isNew==false}"><input type="submit" value="Update"></c:when>
                <c:otherwise><input type="submit" value="Save"></c:otherwise>
            </c:choose>
        </div>
    </form>
</body>
</html>
