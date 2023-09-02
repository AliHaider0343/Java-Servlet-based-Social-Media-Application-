<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>

    <head>
        <title>Friends Feed</title>
        <link rel="stylesheet" type="text/css" href="style.css">
        <script>
            window.addEventListener('load', function () {

                var container = document.getElementById('data-container');
                var xhr = new XMLHttpRequest();
                xhr.onreadystatechange = function () {
                    if (xhr.readyState === XMLHttpRequest.DONE) {
                        if (xhr.status === 200) {
                            var jsonResponse = JSON.parse(xhr.responseText);
                            // Parse JSON response and store values in variables
                            var images = jsonResponse.images;
                            var friendShip_status = jsonResponse.friendShip_status;
                            var current_user = jsonResponse.current_user;
                            var current_user_element = document.getElementById('current_username');
                            current_user_element.innerHTML = current_user.name;
                            var navMenu = document.getElementById('nav-menu');
                            if (current_user.isAdmin) {
                                var adminDashboardLink = document.createElement('li');
                                var adminDashboardAnchor = document.createElement('a');
                                adminDashboardAnchor.setAttribute('href', '${pageContext.request.contextPath}/admin_dashboard_servelet');
                                adminDashboardAnchor.textContent = 'Admin Dashboard';
                                adminDashboardLink.appendChild(adminDashboardAnchor);
                                navMenu.appendChild(adminDashboardLink);
                            }
                            var all_users = jsonResponse.all_users;
                            // Iterate over all_users and create the HTML content
                            var content = '';
                            for (var i = 0; i < all_users.length; i++) {
                                var user = all_users[i];
                                if (user.user_email !== current_user.user_email) {
                                    content += '<div class="card">';
                                    content += '<div class="left">';
                                    content += '<img src="data:image/jpg;base64,' + images[i] + '" alt="Profile Picture Deleted" width="100" height="100">';
                                    content += '</div>';
                                    content += '<div class="right">';
                                    content += '<table >';
                                    content += '<tr><td>Name</td><td>' + user.name + '</td></tr>';
                                    content += '<tr><td>Date of Birth</td><td>' + user.dob + '</td></tr>';
                                    content += '<tr><td>Gender</td><td>' + user.gender + '</td></tr>';
                                    content += '<tr><td>Email</td><td>' + user.user_email + '</td></tr>';
                                    content += '<tr><td>Location</td><td>' + user.location + '</td></tr>';
                                    content += '<tr><td>Admin Status</td><td>' + user.isAdmin + '</td></tr>';

                                    content += '<tr><td>Addresses</td><td>' + user.addresses + '</td></tr>';
                                    content += '<tr><td>Educations</td><td>' + user.educations + '</td></tr>';

                                    content += '</table>';

                                    if (friendShip_status[i]) {
                                        // If the user is already a friend, show a disabled button
                                        content += '<button type="button" disabled>You are already friends</button>';
                                    } else {
                                        content += '<form id="addFriendForm" class="add-friend">';
                                        content += '<input type="hidden" name="friend_email" value="' + user.user_email + '" />';
                                        content += '<input type="hidden" name="current_user_email" value="' + current_user.user_email + '" />';
                                        content += '<button type="button" class="add-friend-button">Make Friend</button>';
                                        content += '</form>';
                                    }

                                    content += '</div>';
                                    content += '</div>';
                                }
                            }
                            container.innerHTML = content;


// Add event listener to Add Friend button
                            var addFriendButtons = document.getElementsByClassName('add-friend-button');

                            for (var j = 0; j < addFriendButtons.length; j++) {
                                addFriendButtons[j].addEventListener('click', function (event) {
                                    event.preventDefault();
                                    var friendEmail = this.form.elements['friend_email'].value;
                                    var currentUserEmail = this.form.elements['current_user_email'].value;
                                    var data = {
                                        'friend_email': friendEmail,
                                        'current_user_email': currentUserEmail
                                    };
                                    var xhr = new XMLHttpRequest();
                                    xhr.onreadystatechange = function () {
                                        if (xhr.readyState === XMLHttpRequest.DONE) {
                                            var response = JSON.parse(xhr.responseText);
                                            var message = response.message;
                                            alert(message);
                                            window.location.replace("users_feed.jsp");

                                        }
                                    };
                                    xhr.open("POST", "add_friend_servelet");
                                    xhr.send(JSON.stringify(data));
                                });
                            }

                            // Add the HTML content to the container element
                        } else {
                            alert("Error Occurred while Processing...");
                        }
                    }
                };


                xhr.open("POST", "feed_servelet", true);
                xhr.send();
            });
        </script>
    </head>
    <body>
        <span style="position: absolute;top:25px;">Logged in as&nbsp;<span id="current_username" >  </span></span>
        <header>
            <nav>
                <ul id="nav-menu">
                    <li><a href="">LOGO</a></li>
                    <li> <a href="${pageContext.request.contextPath}/home_servelet">My Profile</a></li>
                    <li><a href="${pageContext.request.contextPath}/users_feed.jsp">Friends Feed</a></li>
                    <li><a href="${pageContext.request.contextPath}/search_query.jsp">Search Friends</a></li>
                    <li><a href="${pageContext.request.contextPath}/friend_list_servelet">Friend List</a></li>

                    <li><a href="${pageContext.request.contextPath}/logout">Logout</a></li>
                </ul>
            </nav>
        </header>
        <h1>Friends Feed</h1>

        <div id="data-container"></div>

    </body>
</html>
