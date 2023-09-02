<%@page import="java_beans.user"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Education</title>
        <link rel="stylesheet" type="text/css" href="style.css">
        <script>
            function addEducation() {
                var form = document.getElementById("registerForm");
                var xhr = new XMLHttpRequest();
                xhr.onreadystatechange = function () {
                    if (xhr.readyState === XMLHttpRequest.DONE) {
                        var response = JSON.parse(xhr.responseText);
                        var message = response.message;
                        if (response.success) {
                            alert(response.message);
                            window.location.replace("home_servelet");
                        } else {
                            alert(response.message);
                        }
                    }
                };
                xhr.open("POST", "eductaion_servelet");
                xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
                xhr.send(new URLSearchParams(new FormData(form)));
            }

        </script>
    </head>
    <body>
        <span style="position: absolute;top:25px;">Logged in as <%= ((user) session.getAttribute("user")).getName()%></span>
        <header>
            <nav>
                <ul>
                    <li><a href="">LOGO</a></li>
                    <li> <a href="${pageContext.request.contextPath}/home_servelet">My Profile</a></li>
                    <li><a href="${pageContext.request.contextPath}/users_feed.jsp">Friends Feed</a></li>
                    <li><a href="${pageContext.request.contextPath}/search_query.jsp">Search Friends</a></li>
                    <li><a href="${pageContext.request.contextPath}/friend_list_servelet">Friend List</a></li>
                    <c:choose>
                        <c:when test="${  user.isAdmin==1 }">
                            <li><a href="${pageContext.request.contextPath}/admin_dashboard_servelet">Admin Dashboard</a></li>
                        </c:when>
                    </c:choose>
                    <li><a href="${pageContext.request.contextPath}/logout">Logout</a></li>

                </ul>
            </nav>
        </header>
        <h1 style="text-align:center">Set up Your Education!</h1>
        <form id="registerForm" class="register-form" onsubmit="event.preventDefault();
                 addEducation();" >
            <label for="degree">Degree:</label>
            <input type="text" name="degree" id="degree" required=""><br>
            <label for="school">School:</label>

            <input type="text" name="school" id="school" required=""><br>
            <input type="hidden" name="email" value="<%= ((user) session.getAttribute("user")).getUser_email()%>"/>

            <input type="submit" value="Submit">
        </form>

    </body>
</html>
