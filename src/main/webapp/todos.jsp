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
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.7.0/css/all.css" integrity="sha384-lZN37f5QGtY3VHgisS14W3ExzMWZxybE1SJSEsQp9S+oqd12jhcu+A56Ebc1zFSJ" crossorigin="anonymous">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
    <link rel="stylesheet" href="style.css">

    <title>Todo list</title>

</head>
<body>

    <div class="container register">
        <div class="row">
            <div class="col-md-3 register-left text-center">
                <div class="instructions">
                     <h1>Hi ${user.getUserName()}</h1>

                    <c:if test="${user.getTodos().size() == 0}">
                        <h2>Let's Start by adding a Todo!</h2>
                    </c:if>
                </div>
             <a href="todo" class="btnRegister" id="createTodo">Add new Todo</a>
            </div>
            <div class="col-md-9 register-right">
                <div class="tab-content" id="myTabContent">
                <h3 class="register-heading">Your Todos:</h3>


                    <div class="row register-form">

                    <c:if test="${user.getTodos().size() != 0}">
                        <form id="categorySelection" method="Post">
                            <label for="category">Filter by category:</label>
                            <select name="category" id="category">
                                    <option value=""></option>
                                <c:forEach var = "category" items="${user.getDistinctCategories()}">
                                    <option value="${category}">${category}</option>
                                </c:forEach>
                            </select>
                            <input type="submit" value="Submit" class="btnRegister">
                        </form>

                        <table>
                            <thead>
                                <tr>
                                    <th></th>
                                    <th>Title</th>
                                    <th>Category</th>
                                    <th>Due Date</th>
                                    <th>Reserve</th>
                                </tr>
                            </thead>

<%--                        <c:set var = "todos"  value="${user.getTodos()}" /> --%>


                        <c:forEach var = "todo" items="${todos}" >
                            <c:choose>
                                <c:when test="${todo.isCompleted() == true}"><tr class="done"></c:when>
                                <c:otherwise><tr></c:otherwise>
                            </c:choose>
                            <c:choose>
                                <c:when test="${todo.isImportant() == true}">
                                    <td><i class="fas fa-exclamation" style="color: red"></i></td>
                                </c:when>
                                <c:otherwise>
                                    <td></td>
                                </c:otherwise>
                            </c:choose>

                                <td><c:out value = "${todo.getTitle()}"/></td>
                                <td><c:out value = "${todo.getCategory()}"/></td>
                                <td><c:out value = "${todo.getDueDate()}"/></td>

                                <td><a href="todo?todoID=${todo.getTodoID()}">Edit</a></td>
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
