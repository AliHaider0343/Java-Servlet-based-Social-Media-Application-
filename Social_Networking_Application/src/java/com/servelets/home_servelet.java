/**
 *
 * This servlet is responsible for handling the home page requests and retrieving user information.
 *
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
 * <li>java_beans.Address</li>
 * <li>java_beans.education</li>
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
import java_beans.Address;
import java_beans.education;
import java_beans.user;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class home_servelet extends HttpServlet {

    private user_dao userDAO;

    /**
     * Handles the GET request for the home page.
     *
     * @param request the servlet request
     * @param response the servlet response
     * @throws ServletException if an error occurs during the servlet execution
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();

        PrintWriter out = response.getWriter();
        if (session == null || session.getAttribute("loggedIn") == null) {
            out.println("<script>alert('Please Login First and then Access Authorized Pages .')</script>");
            request.getRequestDispatcher("login.jsp").include(request, response);
        } else {
            try {

                user user_obj = (user) session.getAttribute("user");
                if (user_obj != null) {
                    Blob imageBlob = user_obj.getProfile_pic();
                    byte[] imageBytes = imageBlob.getBytes(1, (int) imageBlob.length());
                    String imageBase64 = Base64.getEncoder().encodeToString(imageBytes);
                    userDAO = new user_dao();
                    ArrayList<Address> user_addresses = userDAO.get_Addresses(user_obj.getUser_email());
                    ArrayList<education> user_education = userDAO.get_Education(user_obj.getUser_email());
                    request.setAttribute("profile", imageBase64);
                    request.setAttribute("current_user", user_obj);
                    request.setAttribute("current_user_address", user_addresses);
                    request.setAttribute("current_user_education", user_education);
                }
                request.getRequestDispatcher("home.jsp").forward(request, response);
            } catch (SQLException ex) {
                Logger.getLogger(home_servelet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
