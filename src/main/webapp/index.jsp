<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<!DOCTYPE html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>Todo Application</title>
    <meta name="title" content="Todo Application • Gruppe 1">
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
    <div class="hero-body">
        <div class="container has-text-centered">
            <svg height="32" viewBox="0 0 32 32" width="32" xmlns="http://www.w3.org/2000/svg"><g fill="#fff"><path d="m16 0c-8.83658065 0-16 7.16341935-16 16 0 8.8365806 7.16341935 16 16 16 8.8365806 0 16-7.1634194 16-16 0-8.83658065-7.1634194-16-16-16zm0 3.09677419c7.1310968 0 12.9032258 5.77103226 12.9032258 12.90322581 0 7.1310968-5.7710323 12.9032258-12.9032258 12.9032258-7.13109677 0-12.90322581-5.7710323-12.90322581-12.9032258 0-7.13109677 5.77103226-12.90322581 12.90322581-12.90322581"/><path d="m25.0454194 11.5010968-1.4539355-1.4656774c-.3010968-.30354843-.7912904-.30554843-1.0948387-.0043871l-9.1194839 9.0461935-3.8575484-3.8888387c-.30109677-.3035484-.79129032-.3055484-1.09483871-.0044516l-1.46574193 1.4539355c-.30354839.3010967-.30554839.7912903-.0043871 1.0949032l5.85683874 5.9042581c.3010967.3035483.7912903.3055483 1.0948387.0043871l11.1347742-11.0454194c.3034838-.3011613.3054193-.7913548.0043226-1.0949032z" stroke="#fff" stroke-width=".64"/></g></svg>
            <h1 class="title">Todo Application</h1>
            <img src="img/landing.png" height="852" width="1581" alt="A woman using our amazing Todo Application"/></div>
    </div>
</section>

<section class="section">
    <div class="columns">
        <div class="column is-half is-offset-one-quarter">
            <div class="tabs is-centered">
                <ul>
                    <li class="is-active"><a href="login">Login</a></li>
                    <li><a href="register">Register</a></li>
                </ul>
            </div>

            <h1 class="title">Login</h1>
            <form action="login" method="post">
                <c:if test="${loginFailed == true}">
                    <div class="notification is-warning">
                        <strong>Login unsuccessful. The entered username or password are invalid. Please try again!</strong>
                    </div>
                </c:if>
                <div class="field">
                    <label for="userName" class="label">Username</label>
                    <div class="control has-icons-left has-icons-right">
                        <input name="userName" id="userName" type="text" class="input" placeholder="Username" tabindex="1" required autofocus<c:if test="${userName != null && !userName.isEmpty()}"> value="${userName}"</c:if>>
                        <span class="icon is-small is-left"><i class="fas fa-user"></i></span>
                        <span class="icon is-small is-right"><i class="fas fa-asterisk"></i></span>
                    </div>
                </div>

                <div class="field mb-5">
                    <label for="password" class="label">Password</label>
                    <div class="control has-icons-left has-icons-right">
                        <input name="password" id="password" type="password" class="input" placeholder="Password" tabindex="2" required>
                        <span class="icon is-small is-left"><i class="fas fa-lock"></i></span>
                        <span class="icon is-small is-right"><i class="fas fa-asterisk"></i></span>
                    </div>
                </div>

                <div class="field is-grouped">
                    <div class="control">
                        <input name="btnLogin" type="submit" class="button is-link" tabindex="3" value="Login">
                    </div>
                    <div class="control">
                        <a href="register" class="button is-link is-light">Register</a>
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
