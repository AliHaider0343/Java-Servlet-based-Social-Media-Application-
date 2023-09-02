/**
 *
 * This servlet is responsible for handling the login requests and authenticating the user.
 *
 * <p>
 * <strong>Dependencies:</strong>
 * <ul>
 * <li>Data_access_Objects.user_dao</li>
 * <li>com.google.gson.Gson</li>
 * <li>java.io.BufferedReader</li>
 * <li>java.io.IOException</li>
 * <li>java.io.OutputStream</li>
 * <li>java.io.PrintWriter</li>
 * <li>java.sql.SQLException</li>
 * <li>java.util.Map</li>
 * <li>java.util.logging.Level</li>
 * <li>java.util.logging.Logger</li>
 * <li>java_beans.user</li>
 * <li>javax.servlet.ServletException</li>
 * <li>javax.servlet.http.HttpServlet</li>
 * <li>javax.servlet.http.HttpServletRequest</li>
 * <li>javax.servlet.http.HttpServletResponse</li>
 * <li>javax.servlet.http.HttpSession</li>
 * <li>org.json.JSONException</li>
 * <li>org.json.JSONObject</li>
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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java_beans.user;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONException;
import org.json.JSONObject;

public class login_servelet extends HttpServlet {

    private user_dao userDAO;

    /**
     * Handles the POST request for the login functionality.
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
            userDAO = new user_dao();
            response.setContentType("application/json");
            OutputStream outStream = response.getOutputStream(); // Get the output stream

            StringBuilder sb = new StringBuilder();
            BufferedReader br = request.getReader();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();

            JSONObject jsonObj = new JSONObject(sb.toString());
            String email = jsonObj.getString("email");
            String password = jsonObj.getString("password");
            user user_exist = null;
            try {
                user_exist = userDAO.authenticateUser(email, password);
            } catch (SQLException ex) {
                Logger.getLogger(login_servelet.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (user_exist != null) {
                HttpSession session = request.getSession();
                session.setAttribute("user", user_exist);
                session.setAttribute("loggedIn", true);
                user user_obj = (user) session.getAttribute("user");
                if (user_exist.getIsAdmin() == 1) {
                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("success", true);
                        String object = new Gson().toJson(user_exist);
                        jsonObject.put("user", object);
                        outStream.write(jsonObject.toString().getBytes()); // Write the JSON to the output stream
                        outStream.flush(); // Flush the output stream
                    } catch (JSONException ex) {
                        Logger.getLogger(login_servelet.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("success", true);
                        jsonObject.put("user", new Gson().toJson(user_exist));
                        outStream.write(jsonObject.toString().getBytes()); // Write the JSON to the output stream
                        outStream.flush(); // Flush the output stream
                    } catch (JSONException ex) {
                        Logger.getLogger(login_servelet.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } else {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("success", false);
                } catch (JSONException ex) {
                    Logger.getLogger(login_servelet.class.getName()).log(Level.SEVERE, null, ex);
                }
                outStream.write(jsonObject.toString().getBytes()); // Write the JSON to the output stream
                outStream.flush(); // Flush the output stream
            }
            outStream.close(); // Close the output stream
        } catch (JSONException ex) {
            Logger.getLogger(login_servelet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        userDAO = null;
    }
}
