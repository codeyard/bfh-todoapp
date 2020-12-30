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
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
    <link rel="stylesheet" href="style.css">

    <title>Login successful</title>

</head>
<body>

    <div class="container register">
        <div class="row">
            <div class="col-md-3 register-left text-center">
             <h1>Hi ${user.getUserName()}</h1>

            <c:if test="${user.getTasks().size() == 0}">
                <h2>Let's Start by adding a Task!</h2>
            </c:if>

             <a href="task" class="btnRegister" id="createTask">Add new Task</a>
            </div>
            <div class="col-md-9 register-right">
                <div class="tab-content" id="myTabContent">
                <h3 class="register-heading">Your Tasks:</h3>
                    <div class="row register-form">

                    <c:if test="${user.getTasks().size() != 0}">
                        <table>
                            <thead>
                                <tr>
                                    <th>Title</th>
                                    <th>Category</th>
                                    <th>Due Date</th>
                                    <th>Important</th>
                                    <th>Completed</th>
                                    <th>Reserve</th>
                                </tr>
                            </thead>
                        <c:forEach var = "task" items="${user.getTasks()}" >
                            <c:choose>
                                <c:when test="${task.isCompleted() == true}"><tr class="done"></c:when>
                                <c:otherwise><tr></c:otherwise>
                            </c:choose>


                                <td><c:out value = "${task.getTitle()}"/></td>
                                <td><c:out value = "${task.getCategory()}"/></td>
                                <td><c:out value = "${task.getDueDate()}"/></td>
                                <td><c:out value = "${task.isImportant()}"/></td>
                                <td><c:out value = "${task.isCompleted()}"/></td>
                                <td><a href="task?taskID=${task.getTaskID()}">Edit</a></td>

                            </tr>
                        </c:forEach>

                        </table>
                    </c:if>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
