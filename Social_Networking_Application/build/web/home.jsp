<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="java.sql.Blob"%>
<%@page import="java.util.Base64"%>
<%@page import="java.nio.file.Files"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>User Profile</title>
        <meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" type="text/css" href="style.css">
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

        <div class="user_profile">
            <div class="left">
                <img class="dp" src="data:image/jpg;base64,${profile}" alt="Profile Picture Deleted">
                <h3 style="text-align:center">Display Picture</h3>

            </div>
            <div class="right">
                <table>
                    <tr>
                        <th colspan="2">User Profile</th>
                    </tr>
                    <tr>
                        <td>Name:</td>
                        <td>${current_user.name}</td>
                    </tr>
                    <tr>
                        <td>Date of Birth:</td>
                        <td>${current_user.dob}</td>
                    </tr>
                    <tr>
                        <td>Gender:</td>
                        <td>${current_user.gender}</td>
                    </tr>
                    <tr>
                        <td>Email:</td>
                        <td>${current_user.user_email}</td>
                    </tr>
                    <tr>
                        <td>Location:</td>
                        <td>${current_user.location}</td>
                    </tr>
                    <tr>
                        <td>Admin Status:</td>
                        <td>${current_user.isAdmin}</td>
                    </tr>
                </table>
            </div>
        </div>




        <h1>User Addresses</h1>
        <button onclick="location.href = '${pageContext.request.contextPath}/add_address.jsp'">Add  Address Information</button>
        <table>
            <thead>
                <tr>
                    <th>Street</th>
                    <th>Town</th>
                    <th>State</th>
                    <th>Country</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="address" items="${current_user_address}">
                    <tr>
                        <td><c:out value="${address.getStreet()}" /></td>
                        <td><c:out value="${address.town}" /></td>
                        <td><c:out value="${address.state}" /></td>
                        <td><c:out value="${address.country}" /></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <h1>User Educations</h1>
        <button onclick="location.href = '${pageContext.request.contextPath}/add_education.jsp'">Add  Education Information</button>
        <table>
            <thead>
                <tr>
                    <th>Degree</th>
                    <th>School</th>

                </tr>
            </thead>
            <tbody>
                <c:forEach var="education" items="${current_user_education}">
                    <tr>
                        <td><c:out value="${education.degree}" /></td>
                        <td><c:out value="${education.school}" /></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>



    </body>
</html>

