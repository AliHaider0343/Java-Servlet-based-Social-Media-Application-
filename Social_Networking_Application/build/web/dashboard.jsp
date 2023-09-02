<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
    <head>
        <title>Admin Dashboard</title>
        <link rel="stylesheet" type="text/css" href="style.css">
        <script>
            function deleteUser(email) {
                var xhr = new XMLHttpRequest();
                xhr.onreadystatechange = function () {
                    if (xhr.readyState === XMLHttpRequest.DONE) {
                        if (xhr.status === 200) {
                            var response = JSON.parse(xhr.responseText);
                            if (response.error) {
                                alert(response.error);
                            } else {
                                alert(response.message);
                                window.location.replace("admin_dashboard_servelet");

                            }
                        } else {
                            alert("An error occurred while deleting the user.");
                        }
                    }
                };
                xhr.open("POST", "delete_user_servelet");
                xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
                xhr.send("user_to_act_email=" + email);
            }

            function deleteUserdp(email) {
                var xhr = new XMLHttpRequest();
                xhr.onreadystatechange = function () {
                    if (xhr.readyState === XMLHttpRequest.DONE) {
                        if (xhr.status === 200) {
                            var response = JSON.parse(xhr.responseText);
                            if (response.error) {
                                alert(response.error);
                            } else {
                                alert(response.message);
                                window.location.replace("admin_dashboard_servelet");
                            }
                        } else {
                            alert("An error occurred while deleting the user Profile.");
                        }
                    }
                };
                xhr.open("POST", "delete_user_dp_servelet");
                xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
                xhr.send("user_to_act_email=" + email);
            }

        </script>
    </head>
    <body>
        <span style="position: absolute;top:25px;">Logged in as ${current_user.name}</span>
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


        <h1 >Administrator Dashboard</h1>
        <button onclick="location.href = '${pageContext.request.contextPath}/add_user_by_admin.jsp'">Add New User</button>
        <h2 >Registered Accounts Details</h2>

        <c:forEach var="user" items="${all_users}"  varStatus="status">
            <c:choose>
                <c:when test="${  !user.user_email.equals( current_user.user_email) }">
                    <div class="card">
                        <div class="left">
                            <img src="data:image/jpg;base64,${images.get(status.index)}" alt="Profile Picture Deleted" width="100" height="100">
                        </div>
                        <div class="right">
                            <table>
                                <tr><td>Name</td><td><c:out value="${user.name}" /></td></tr>
                                <tr><td>Date of Birth</td><td><c:out value="${user.dob}" /></td></tr>
                                <tr><td>Gender</td><td><c:out value="${user.gender}" /></td></tr>
                                <tr> <td>Email</td><td><c:out value="${user.user_email}" /></td></tr>
                                <tr><td>Location</td><td><c:out value="${user.location}" /></td></tr>
                                <tr><td>Admin Status</td><td><c:out value="${user.isAdmin}" /></td></tr>
                                <tr><td>Addresses</td><td><c:out value="${user.getAddresses()}" /></td></tr>
                                <tr><td>Educations</td><td><c:out value="${user.getEducations()}" /></td></tr>
                            </table>

                            <button onclick="deleteUser('${user.user_email}')">Delete User Account</button>
                            <button onclick="deleteUserdp('${user.user_email}')">Delete User Profile Picture</button>

                            <form action="update_user_info_servelet" method="POST">
                                <input type="hidden" name="user_to_act_email" value="${user.user_email}" />
                                <input type="hidden" name="current_user_email" value="${current_user.user_email}" />
                                <button type="submit">Update user Account</button>
                            </form>
                        </div>
                    </div>
                </c:when>
            </c:choose>
        </c:forEach>
    </body>
</html>
