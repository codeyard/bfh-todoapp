<%--
  Created by IntelliJ IDEA.
  User: raphaelgerber
  Date: 28.12.20
  Time: 13:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <c:choose>
        <c:when test="${todo.getTitle() == null}">
            <title>Todo Application • Add new Todo</title>
            <meta name="title" content="Todo Application • Add new Todo">
        </c:when>
        <c:otherwise>
            <title>Todo Application • Edit Todo</title>
            <meta name="title" content="Todo Application • Edit Todo">
        </c:otherwise>
    </c:choose>
    <meta name="description" content="Todo Application • CAS Software Development (SD-HS20) • Gruppe 1">
    <meta name="author" content="Igor Stojanovic, Sabina Löffel, Christophe Leupi, Raphael Gerber">
    <meta name="keywords" content="berner fachhochschule, bfh, cas software development, sd-hs20, herbst 2020, igor stojanovic, sabina löffel, christophe leupi, raphael gerber, bern university of applied science." />

    <link rel="stylesheet" href="css/bulma.min.css">
    <link rel="stylesheet" href="css/styles.css">

    <!-- Favicons -->
    <link rel="apple-touch-icon" href="favicon/apple-touch-icon.png">
    <link rel="apple-touch-icon" sizes="180x180" href="favicon/apple-touch-icon-180x180.png">
    <meta name="apple-mobile-web-app-title" content="TodoApp">
    <link rel="mask-icon" href="favicon/safari-pinned-tab.svg" color="#4c9ebf">
    <link rel="alternate icon" type="image/png" href="favicon/favicon.png">
    <link rel="icon" type="image/svg+xml" href="favicon/favicon.svg">
    <link rel="manifest" href="manifest.webmanifest">
    <meta name="msapplication-TileColor" content="#1d1e1f">
    <meta name="msapplication-config" content="favicon/browserconfig.xml">
    <meta name="theme-color" content="#1d1e1f">
    <link rel="icon" type="image/png" sizes="32x32" href="favicon/favicon-32x32.png">
    <link rel="icon" type="image/png" sizes="16x16" href="favicon/favicon-16x16.png">
    <meta name="thumbnail" content="favicon/thumb-150x150.png">
</head>

<body>
<section class="hero is-info is-bold">
    <div class="hero-head">
        <nav class="level is-mobile pt-5 pr-5">
            <div class="level-left"></div>
            <div class="level-right">
                <p class="level-item">${user.getUserName()}</p>
                <p class="level-item"><a href="logout" class="button">Logout</a></p>
            </div>
        </nav>
    </div>

    <div class="hero-body pt-5 pr-5 pb-6 pl-5">
        <div class="container has-text-centered">
            <svg height="32" viewBox="0 0 32 32" width="32" xmlns="http://www.w3.org/2000/svg"><g fill="#fff"><path d="m16 0c-8.83658065 0-16 7.16341935-16 16 0 8.8365806 7.16341935 16 16 16 8.8365806 0 16-7.1634194 16-16 0-8.83658065-7.1634194-16-16-16zm0 3.09677419c7.1310968 0 12.9032258 5.77103226 12.9032258 12.90322581 0 7.1310968-5.7710323 12.9032258-12.9032258 12.9032258-7.13109677 0-12.90322581-5.7710323-12.90322581-12.9032258 0-7.13109677 5.77103226-12.90322581 12.90322581-12.90322581"/><path d="m25.0454194 11.5010968-1.4539355-1.4656774c-.3010968-.30354843-.7912904-.30554843-1.0948387-.0043871l-9.1194839 9.0461935-3.8575484-3.8888387c-.30109677-.3035484-.79129032-.3055484-1.09483871-.0044516l-1.46574193 1.4539355c-.30354839.3010967-.30554839.7912903-.0043871 1.0949032l5.85683874 5.9042581c.3010967.3035483.7912903.3055483 1.0948387.0043871l11.1347742-11.0454194c.3034838-.3011613.3054193-.7913548.0043226-1.0949032z" stroke="#fff" stroke-width=".64"/></g></svg>
            <h1 class="title">Todo Application</h1>
        </div>
    </div>
</section>


<section class="section">
    <div class="columns">
        <div class="column is-half is-offset-one-quarter">
            <c:choose>
                <c:when test="${todo.getTitle() == null}">
                    <h1 class="title">Add new Todo</h1>
                </c:when>
                <c:otherwise>
                    <h1 class="title">Edit Todo</h1>
                </c:otherwise>
            </c:choose>

            <form action="todo" method="post">
                <input type="hidden" name="todoID" value="${todo.getTodoID()}">
                <input type="hidden" name="isNew" value="${isNew}">

                <c:if test="${dateError==true}">
                    <div class="notification is-warning">
                        <strong>The due date entered is invalid!</strong>
                    </div>
                </c:if>

                <div class="field">
                    <label for="title" class="label">Title</label>
                    <div class="control">
                        <input name="title" id="title" type="text" class="input" placeholder="Title" value="${todo.getTitle()}" tabindex="1" required autofocus>
                    </div>
                    <p class="help">This field is required</p>
                </div>

                <c:choose>
                    <c:when test="${user.getDistinctCategories().size() > 0}">
                        <div class="field">
                            <label for="categorySelect" class="label">Category</label>
                            <div class="control">
                                <div class="select is-fullwidth">
                                    <select name="category" id="categorySelect" tabindex="2">
                                        <option value="">Choose or enter a new one below</option>
                                        <c:forEach var="category" items="${user.getDistinctCategories()}">
                                            <option value="${category}" <c:if test="${category.equals(todo.getCategory())}">selected</c:if>>${category}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                        </div>
                        <div class="field">
                            <div class="control">
                                <input name="newCategory" type="text" class="input" placeholder="New Category" tabindex="3">
                            </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="field">
                            <label for="category" class="label">Category</label>
                            <div class="control">
                                <input name="category" id="category" type="text" class="input" placeholder="Category" tabindex="2">
                            </div>
                        </div>
                    </c:otherwise>
                </c:choose>

                <c:choose>
                    <c:when test="${dateError==true}">
                        <div class="field mb-5">
                            <label for="dueDateErr" class="label">Due Date</label>
                            <div class="control">
                                <input name="dueDate" id="dueDateErr" type="date" class="input is-danger" placeholder="Due Date" value="${todo.getDueDate()}" tabindex="4">
                            </div>
                            <p class="help is-danger">The due date entered is invalid!</p>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="field mb-5">
                            <label for="dueDate" class="label">Due Date</label>
                            <div class="control">
                                <input name="dueDate" id="dueDate" type="date" class="input" placeholder="Due Date" value="${todo.getDueDate()}" tabindex="4">
                            </div>
                        </div>
                    </c:otherwise>
                </c:choose>

                <div class="field mb-5">
                    <div class="control">
                        <label for="isImportant" class="checkbox">
                            <input name="isImportant" id="isImportant" type="checkbox" <c:if test="${todo.isImportant()==true}">checked</c:if>> Todo is important
                        </label>
                    </div>
                </div>

                <c:if test="${isNew==false}">
                    <div class="field mb-5">
                        <div class="control">
                            <label for="isCompleted" class="checkbox">
                                <input name="isCompleted" id="isCompleted" type="checkbox" <c:if test="${todo.isCompleted()==true}">checked</c:if>> Todo is completed
                            </label>
                        </div>
                    </div>
                </c:if>

                <div class="field is-grouped">
                    <div class="control">
                        <input name="save" type="submit" class="button is-success" tabindex="5" value="Save">
                    </div>
                    <c:if test="${isNew==false}">
                        <div class="control">
                            <input name="Delete" type="submit" class="button is-danger" value="Delete">
                        </div>
                    </c:if>
                    <div class="control">
                        <a href="todos" class="button is-link is-light">Cancel</a>
                    </div>
                </div>
            </form>
        </div>
    </div>
</section>

<footer class="footer">
    <div class="content has-text-centered">
        <p>
            &copy; 2021 by <strong>CAS SD-HS20 • Gruppe 1</strong>: Igor Stojanovic, Sabina L&ouml;ffel, Christophe Leupi &amp; Raphael Gerber.
        </p>
    </div>
</footer>

<script defer src="https://use.fontawesome.com/releases/v5.14.0/js/all.js"></script>
</body>
</html>
