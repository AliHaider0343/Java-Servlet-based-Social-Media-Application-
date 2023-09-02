/**
 *
 * This servlet is responsible for modifying user information by an admin.
 * <p>
 * <strong>Dependencies:</strong>
 * <ul>
 * <li>Data_access_Objects.user_dao</li>
 * <li>com.google.gson.Gson</li>
 * <li>com.google.gson.GsonBuilder</li>
 * <li>java.io.ByteArrayOutputStream</li>
 * <li>java.io.IOException</li>
 * <li>java.io.InputStream</li>
 * <li>java.io.PrintWriter</li>
 * <li>java.sql.Blob</li>
 * <li>java.sql.SQLException</li>
 * <li>java.util.HashMap</li>
 * <li>java.util.Map</li>
 * <li>java.util.logging.Level</li>
 * <li>java.util.logging.Logger</li>
 * <li>java_beans.user</li>
 * <li>javax.servlet.ServletException</li>
 * <li>javax.servlet.annotation.MultipartConfig</li>
 * <li>javax.servlet.http.HttpServlet</li>
 * <li>javax.servlet.http.HttpServletRequest</li>
 * <li>javax.servlet.http.HttpServletResponse</li>
 * <li>javax.servlet.http.Part</li>
 * <li>javax.sql.rowset.serial.SerialBlob</li>
 * </ul>
 * </p>
 *
 * @author Jose Omar
 * @version 1.0
 * @since 2023-05-14
 */
package com.servelets;

import Data_access_Objects.user_dao;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
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
public class update_user_by_admin_servelet extends HttpServlet {

    private user_dao userDAO;

    /**
     * Handles the POST request for modifying user information by an admin.
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
            String user_to_act_email = request.getParameter("user_to_act_email");
            String name = request.getParameter("name");
            String dob = request.getParameter("dob");
            String gender = request.getParameter("gender");
            String email = user_to_act_email;
            String password = request.getParameter("password");
            String confpassword = request.getParameter("confirmPassword");
            String location = request.getParameter("location");
            String checkBoxValue = request.getParameter("role");
            int isadmin = 0; // initialize the integer value to 0 by default
            if (checkBoxValue != null && checkBoxValue.equals("1")) {
                isadmin = 1;
            } else if (checkBoxValue == null) {
                isadmin = 0;
            } else if (checkBoxValue != null && checkBoxValue.equals("on")) {
                isadmin = 1; // set the integer value to 1 if the checkbox is checked
            }
            System.out.println("heeeeeeeeeeeeeeeeeeeeeeee" + checkBoxValue);

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

            Map<String, Object> jsonResponse = new HashMap<>();
            Gson gson = new GsonBuilder().create();

            if (!password.equals(confpassword)) {
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Password and Confirm Password Doesn't Match.");
            } else {
                userDAO = new user_dao();

                user newUser = new user(name, dob, gender, email, profilePicBlob, location, password, isadmin);
                if (userDAO.updateUser(newUser, user_to_act_email) > 0) {
                    jsonResponse.put("success", true);
                    jsonResponse.put("message", "The Account has been updated with Specified Information.");
                } else {
                    jsonResponse.put("success", false);
                    jsonResponse.put("message", "An Error Occurred While Processing Request.");
                }
            }
            String json = gson.toJson(jsonResponse);
            out.print(json);
            out.flush();
        } catch (SQLException ex) {
            Logger.getLogger(register_servelet.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
