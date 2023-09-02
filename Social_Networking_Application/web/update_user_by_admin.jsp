<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>User Modification by Admin</title>                <link rel="stylesheet" type="text/css" href="style.css">
        <script>
            function updateUser() {
                var form = document.getElementById("registerForm");
                var formData = new FormData(form);

                var xhr = new XMLHttpRequest();
                xhr.onreadystatechange = function () {
                    if (xhr.readyState === XMLHttpRequest.DONE) {
                        var response = JSON.parse(xhr.responseText);
                        var message = response.message;
                        alert(message);
                        if (response.success) {
                            window.location.replace("admin_dashboard_servelet");
                        }
                    }
                };
                xhr.open("POST", "update_user_by_admin_servelet");
                xhr.send(formData);
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

        <h1>Modify user Information</h1>
        <form id="registerForm" class="register-form" onsubmit="event.preventDefault();
                   updateUser();" enctype="multipart/form-data">
            <label for="name">Name:</label>
            <input type="text" name="name" value="${user_to_update.name}" required><br><br>
            <label for="dob">Date of Birth:</label>
            <input type="date" name="dob" value="${user_to_update.dob}" required><br><br>
            <label for="gender">Gender:</label>
            <input type="radio" name="gender" value="Male" ${user_to_update.gender == 'Male' ? 'checked' : ''}> Male
            <input type="radio" name="gender" value="Female"   ${user_to_update.gender == 'Female' ? 'checked' : ''}> Female
            <input type="radio" name="gender" value="Other"  ${user_to_update.gender == 'Other' ? 'checked' : ''}> Other<br><br>
            <label for="email">Email Address:</label>
            <input type="email" name="email" value="${user_to_update.user_email}"  disabled><br><br>
            <input type="hidden" name="user_to_act_email" value="${user_to_update.user_email}"" />
            <label for="profilePic">Profile Picture:</label>
            <input type="file" name="profilePic" accept="image/jpeg" required><br><br>
            <label for="location">Location:</label>
            <input type="text" name="location" value="${user_to_update.location}"  required><br><br>
            <label for="password">Password:</label>
            <input type="password" name="password" value="****" required><br><br>
            <label for="confirmPassword">Confirm Password:</label>
            <input type="password" name="confirmPassword" value="****" required><br><br>
            <label for="dob">Role (Check to Privilage user as Admin) </label>
            <input type="checkbox" name="role" ${user_to_update.isAdmin ==1 ? 'checked' : ''} ><br><br>
            <input type="submit" value="Add User">
        </form>
    </body>
</html>
