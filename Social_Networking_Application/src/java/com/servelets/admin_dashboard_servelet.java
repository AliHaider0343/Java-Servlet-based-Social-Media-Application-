/**
 *
 * This servlet is responsible for retrieving user account details to be displayed on the admin page.
 * <p>
 * <strong>Dependencies:</strong>
 * <ul>
 * <li>Data_access_Objects.user_dao</li>
 * <li>java.io.IOException</li>
 * <li>java.io.PrintWriter</li>
 * <li>java.sql.Blob</li>
 * <li>java.sql.SQLException</li>
 * <li>java.util.ArrayList</li>
 * <li>java.util.Base64</li>
 * <li>java.util.logging.Level</li>
 * <li>java.util.logging.Logger</li>
 * <li>java_beans.user</li>
 * <li>javax.servlet.ServletException</li>
 * <li>javax.servlet.http.HttpServlet</li>
 * <li>javax.servlet.http.HttpServletRequest</li>
 * <li>javax.servlet.http.HttpServletResponse</li>
 * <li>javax.servlet.http.HttpSession</li>
 * </ul>
 * </p>
 *
 * @author Jose Omar
 * @version 1.0
 * @since 2023-05-14
 */
package com.servelets;

import Data_access_Objects.user_dao;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import java_beans.user;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class admin_dashboard_servelet extends HttpServlet {

    private user_dao userDAO;
    user user_obj = null;

    /**
     * Handles the GET request for displaying user account details on the admin
     * page.
     *
     * @param request the servlet request
     * @param response the servlet response
     * @throws ServletException if an error occurs during the servlet execution
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();

        if (session == null || session.getAttribute("loggedIn") == null) {
            out.println("<script>alert('Please Login First and then Access Authorized Pages .')</script>");
            request.getRequestDispatcher("login.jsp").include(request, response);
        } else if (session != null) {
            user_obj = (user) session.getAttribute("user");
            if (user_obj.getIsAdmin() != 1) {
                out.println("<script>alert('You are not Authorized to Logged in as Administrator Please Login as Admin.')</script>");
                request.getRequestDispatcher("login.jsp").include(request, response);
            } else {
                try {

                    userDAO = new user_dao();
                    ArrayList<user> users = userDAO.getAll_users();
                    user user_obj = (user) session.getAttribute("user");
                    ArrayList<String> images = new ArrayList<String>();
                    ArrayList<Boolean> friend_ship_status = new ArrayList<Boolean>();
                    if (user_obj != null) {
                        for (int i = 0; i < users.size(); i++) {
                            Blob imageBlob = users.get(i).getProfile_pic();
                            if (imageBlob != null) {
                                byte[] imageBytes = imageBlob.getBytes(1, (int) imageBlob.length());
                                String imageBase64 = Base64.getEncoder().encodeToString(imageBytes);
                                images.add(imageBase64);
                            }

                            friend_ship_status.add(userDAO.isFriend(user_obj.getUser_email(), users.get(i).getUser_email()));
                        }
                        request.setAttribute("images", images);
                        request.setAttribute("friendShip_status", friend_ship_status);
                        request.setAttribute("current_user", user_obj);
                        request.setAttribute("all_users", users);
                    }
                    request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                } catch (SQLException ex) {
                    Logger.getLogger(home_servelet.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
