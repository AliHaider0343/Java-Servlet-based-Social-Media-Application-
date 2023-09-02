
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
        <meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>User Registration</title>
        <link rel="stylesheet" type="text/css" href="style.css"><script>
            function registerUser() {
                var form = document.getElementById("registerForm");
                var formData = new FormData(form);

                var xhr = new XMLHttpRequest();
                xhr.onreadystatechange = function () {
                    if (xhr.readyState === XMLHttpRequest.DONE) {
                        var response = JSON.parse(xhr.responseText);
                        var message = response.message;
                        alert(message);
                        if (response.success) {
                            window.location.replace("register.jsp");
                        }
                    }
                };
                xhr.open("POST", "register_servelet");
                xhr.send(formData);
            }
        </script>
    </head>
    <body>
        <h1 style="text-align:center">User Registration</h1>
        <form id="registerForm" class="register-form" onsubmit="event.preventDefault();
                registerUser();" enctype="multipart/form-data">
            <label for="name">Name:</label>
            <input type="text" name="name" required><br><br>
            <label for="dob">Date of Birth:</label>
            <input type="date" name="dob" required><br><br>
            <label for="gender">Gender:</label>
            <input type="radio" name="gender" value="Male" checked> Male
            <input type="radio" name="gender" value="Female"> Female
            <input type="radio" name="gender" value="Other"> Other<br><br>
            <label for="email">Email Address:</label>
            <input type="email" name="email" required><br><br>
            <input type="hidden" name="isAdmin" value="0"><br><br>
            <label for="profilePic">Profile Picture:</label>
            <input type="file" name="profilePic" accept="image/jpeg" required><br><br>
            <label for="location">Location:</label>
            <input type="text" name="location" required><br><br>
            <label for="password">Password:</label>
            <input type="password" name="password" required><br><br>
            <label for="confirmPassword">Confirm Password:</label>
            <input type="password" name="confirmPassword" required><br><br>
            <input type="submit" value="Register">
        </form>
        <a class="navigators" href="login.jsp">Already have an Account login ?</a>        <a  class="navigators" href="index.html">Get to the Startup Page</a>

    </body>
</html>
