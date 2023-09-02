/**
 *
 * This servlet is responsible for handling the registration requests and adding new users to the database.
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
public class register_servelet extends HttpServlet {

    private user_dao userDAO;

    /**
     * Handles the POST request for the registration functionality.
     *
     * @param request the servlet request
     * @param response the servlet response
     * @throws ServletException if an error occurs during the servlet execution
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        String name = request.getParameter("name");
        String dob = request.getParameter("dob");
        String gender = request.getParameter("gender");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confpassword = request.getParameter("confirmPassword");
        String location = request.getParameter("location");
        String isadmin = request.getParameter("isAdmin");
        Blob profilePicBlob = null;
        try {
            Part imagePart = request.getPart("profilePic");
            InputStream imageInputStream = imagePart.getInputStream();
            ByteArrayOutputStream imageBytesOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = imageInputStream.read(buffer)) != -1) {
                imageBytesOutputStream.write(buffer, 0, bytesRead);
            }
            byte[] imageBytes = imageBytesOutputStream.toByteArray();
            profilePicBlob = new javax.sql.rowset.serial.SerialBlob(imageBytes);

        } catch (Exception ex) {
            System.out.println("Permisson revoked for Deletion.");
        }
        Map<String, Object> jsonResponse = new HashMap<>();
        Gson gson = new GsonBuilder().create();
        if (!password.equals(confpassword)) {
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Password and Confirm Password Doesn't Match.");
        } else {
            userDAO = new user_dao();
            try {
                if (!userDAO.user_exists(email)) {
                    user newUser = new user(name, dob, gender, email, profilePicBlob, location, password, Integer.parseInt(isadmin));
                    if (userDAO.addUser(newUser) > 0) {
                        jsonResponse.put("success", true);
                        jsonResponse.put("message", "Your Account has been Created Successfully.");
                    } else {
                        jsonResponse.put("success", false);
                        jsonResponse.put("message", "An Error Occurred While Processing.");
                    }
                } else {
                    jsonResponse.put("success", false);
                    jsonResponse.put("message", "Account Already Exists with Given Mail. Please Use Another Email Address.");
                }
            } catch (SQLException ex) {
                Logger.getLogger(register_servelet.class.getName()).log(Level.SEVERE, null, ex);
                jsonResponse.put("success", false);
                jsonResponse.put("message", "An Error Occurred While Processing Request.");
            }
        }
        String json = gson.toJson(jsonResponse);
        out.print(json);
        out.flush();
    }

    @Override
    public void destroy() {
        super.destroy();
        userDAO = null;
    }

}
