 
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@page import="java.io.*"%>
<%@page import="java.net.*"%>
<%@page import="javax.servlet.*"%>
<%@page import="javax.servlet.http.*"%>
<%@page import="org.json.*"%>
<%@page import="com.google.gson.*"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>User Login</title>
        <link rel="stylesheet" type="text/css" href="style.css">
        <script>
            function login() {
                var email = document.getElementsByName("email")[0].value;
                var password = document.getElementsByName("password")[0].value;
                var xhr = new XMLHttpRequest();
                xhr.open("POST", "login_servelet", true);
                xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
                xhr.onreadystatechange = function () {
                    if (this.readyState === XMLHttpRequest.DONE && this.status === 200) {
                        var response = JSON.parse(this.responseText);
                        if (response.success) {
                            var user = JSON.parse(response.user);
                            if (user.isAdmin == 1) {
                                window.location.replace("admin_dashboard_servelet");
                            } else {
                                window.location.replace("home_servelet");
                            }
                        } else {
                            alert("Invalid email or password. Please try again.");
                            window.location.replace("login.jsp");
                        }
                    }
                };
                var params = '{"email":"' + email + '","password":"' + password + '"}';
                xhr.send(params);
            }

        </script>

    </head>
    <body>
        <h1 style="text-align:center">User Login</h1>
        <form class="login-form" onsubmit="login();
                return false;">
            <label for="email">Email Address:</label>
            <input type="email" name="email" required><br><br>
            <label for="password">Password:</label>
            <input type="password" name="password" required><br><br>
            <input type="submit" value="Login">
        </form>

        <a class="navigators" href="register.jsp">Doesn't have an Account Get Registered ?</a>  
        <a class="navigators" href="index.html">Get to the Startup Page</a>
    </body>
</html>

