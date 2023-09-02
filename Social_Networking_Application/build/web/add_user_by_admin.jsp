<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" type="text/css" href="style.css">
        <title>User Registration by Admin</title>
    </head>

    <body>
        <span style="position: absolute;top:25px;">Logged in as ${current_user.name}</span>
        <header>
            <nav>
                <ul>
                    <li>LOGO</li>
                    <li><a href="${pageContext.request.contextPath}/logout">Logout</a></li>
                    <li> <a href="${pageContext.request.contextPath}/home_servelet">My Profile</a></li>
                    <li><a href="${pageContext.request.contextPath}/users_feed.jsp">Friends Feed</a></li>
                    <li><a href="${pageContext.request.contextPath}/search_query.jsp">Search Friends</a></li>
                    <li><a href="${pageContext.request.contextPath}/friend_list_servelet">Friend List</a></li>
                    <li><a href="${pageContext.request.contextPath}/admin_dashboard_servelet">Admin Dashboard</a></li>
                </ul>
            </nav>
        </header>

        <h1 style="text-align:center">Add user</h1>
        <form class="register-form" action="add_user_by_admin_servelet" method="post" enctype="multipart/form-data" >
            <label for="name">Name:</label>
            <input type="text" name="name" required><br><br>
            <label for="dob">Date of Birth:</label>
            <input type="date" name="dob" required><br><br>
            <label for="gender">Gender:</label>
            <input type="radio" name="gender" value="male" checked> Male
            <input type="radio" name="gender" value="female"> Female
            <input type="radio" name="gender" value="other"> Other<br><br>
            <label for="email">Email Address:</label>
            <input type="email" name="email" required><br><br>
            <label for="profilePic">Profile Picture:</label>
            <input type="file" name="profilePic" accept="image/jpeg" required><br><br>
            <label for="location">Location:</label>
            <input type="text" name="location" required><br><br>
            <label for="password">Password:</label>
            <input type="password" name="password" required><br><br>
            <label for="confirmPassword">Confirm Password:</label>
            <input type="password" name="confirmPassword" required><br><br>
            <label for="dob">Role (Check to Privilage user as Admin) </label>
            <input type="checkbox" name="role" ><br><br>
            <input type="submit" value="Add User">
        </form>
    </body>
</html>
