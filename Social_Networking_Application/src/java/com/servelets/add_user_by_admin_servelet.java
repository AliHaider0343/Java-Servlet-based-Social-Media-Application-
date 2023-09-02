/**
 *
 * This servlet is responsible for adding a user to the database by the admin account. It receives
 * user details including name, date of birth, gender, email, password, confirm password, location,
 * role, and profile picture. It validates the password and confirm password, checks if the email
 * already exists, and adds the user to the database using the user data access object.
 * <p>
 * <strong>Dependencies:</strong>
 * <ul>
 * <li>Data_access_Objects.user_dao</li>
 * <li>java.io.ByteArrayOutputStream</li>
 * <li>java.io.IOException</li>
 * <li>java.io.InputStream</li>
 * <li>java.io.PrintWriter</li>
 * <li>java.sql.Blob</li>
 * <li>java.sql.SQLException</li>
 * <li>java.util.logging.Level</li>
 * <li>java.util.logging.Logger</li>
 * <li>java_beans.user</li>
 * <li>javax.servlet.ServletException</li>
 * <li>javax.servlet.annotation.MultipartConfig</li>
 * <li>javax.servlet.http.HttpServlet</li>
 * <li>javax.servlet.http.HttpServletRequest</li>
 * <li>javax.servlet.http.HttpServletResponse</li>
 * <li>javax.servlet.http.Part</li>
 * </ul>
 * </p>
 *
 * @author Jose Omar
 * @version 1.0
 * @since 2023-05-14
 */
package com.servelets;

import Data_access_Objects.user_dao;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java_beans.user;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@MultipartConfig(maxFileSize = 16177216)//1.5mb
public class add_user_by_admin_servelet extends HttpServlet {

    private user_dao userDAO;

    /**
     * Handles the POST request for adding a user by the admin.
     *
     * @param request the servlet request
     * @param response the servlet response
     * @throws ServletException if an error occurs during the servlet execution
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();

            String name = request.getParameter("name");
            String dob = request.getParameter("dob");
            String gender = request.getParameter("gender");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String confpassword = request.getParameter("confirmPassword");
            String location = request.getParameter("location");
            String checkBoxValue = request.getParameter("role");
            int isadmin = 0;
            if (checkBoxValue != null && checkBoxValue.equals("on")) {
                isadmin = 1;
            }
            Part imagePart = request.getPart("profilePic");
            InputStream imageInputStream = imagePart.getInputStream();
            ByteArrayOutputStream imageBytesOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = imageInputStream.read(buffer)) != -1) {
                imageBytesOutputStream.write(buffer, 0, bytesRead);
            }
            byte[] imageBytes = imageBytesOutputStream.toByteArray();
            Blob profilePicBlob = new javax.sql.rowset.serial.SerialBlob(imageBytes);

            if (!password.equals(confpassword)) {
                out.println("<script>alert('Password and Confirm Password Doesnt Match .')</script> ");
            } else {
                userDAO = new user_dao();
                try {
                    if (!userDAO.user_exists(email)) {
                        user newUser = new user(name, dob, gender, email, profilePicBlob, location, password, isadmin);
                        if (userDAO.addUser(newUser) > 0) {
                            out.println("<script>alert('The Account has been Added with Specified Information.')</script> ");
                            response.sendRedirect("admin_dashboard_servelet");
                        } else {
                            out.println("<script>alert('An Error Accured While Processing.')</script> ");
                        }
                    } else {
                        out.println("<script>alert('Account Already Exist with Given Mail Please use Another Mailing Address.')</script> ");
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(register_servelet.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            request.getRequestDispatcher("add_user_by_admin.jsp").include(request, response);

        } catch (SQLException ex) {
            Logger.getLogger(register_servelet.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
