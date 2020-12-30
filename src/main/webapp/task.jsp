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
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
    <link rel="stylesheet" href="style.css">
    <title>Edit or create Task</title>
</head>
<body>
    <div class="container register">
        <div class="row">
            <div class="col-md-3 register-left">
                <h3>Current Task:</h3>
                <c:choose>
                    <c:when test="${task.getTitle() == null}"><h3>New Task</h3></c:when>
                    <c:otherwise><h3>${task.getTitle()}</h3></c:otherwise>
                </c:choose>
            </div>

            <div class="col-md-9 register-right">
                <div class="tab-content" id="myTabContent">
                    <h3 class="register-heading">Edit Information</h3>
                <form action="task" method="post">
                <div class="row register-form">
                    <div class="col-md-6">
                    <div class="form-group">
                            <input type="hidden" name="taskID" value="${task.getTaskID()}">
                            <input type="hidden" name="isNew" value="${isNew}">

                            <div class="task">
                                <label for="title">Title:</label>
                                <input id="title" name="title" type="text" placeholder="title" value="${task.getTitle()}">
                            </div>

                            <div class="task">
                                <label for="category">Category:</label>
                                <input id="category" name="category" type="text" placeholder="category" value="${task.getCategory()}">
                            </div>

                            <div class="task">
                                <label for="dueDate">Due Date:</label>
                                <input id="dueDate" name="dueDate" type="date" placeholder="dueDate" value="${task.getDueDate()}">
                            </div>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="form-group">
                        <div class="task">
                            <label for="isImportant">Is your Task Important?</label>
                            <input id="isImportant" name="isImportant" type="checkbox" value="true" <c:if test="${task.isImportant()==true}">checked</c:if>>
                        </div>

                        <c:if test="${isNew==false}">
                            <div class="task">
                                <label for="isCompleted">Is your Task Completed?</label>
                                <input id="isCompleted" name="isCompleted" type="checkbox" value="true" <c:if test="${task.isCompleted()==true}">checked</c:if>>
                            </div>
                        </c:if>
                    </div>
                        <div>
                            <c:choose>
                                <c:when test="${isNew==false}">
                                    <input type="submit" value="Update" class="btnRegister">
                                    <input type="submit" value="Delete" name="Delete" class="btnRegister" id="btnDelete">
                                </c:when>
                                <c:otherwise><input type="submit" value="Save" class="btnRegister"></c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </form>
        </div>
        </div>
    </div>
    </div>

</body>
</html>
