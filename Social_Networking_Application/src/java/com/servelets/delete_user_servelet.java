/**
 *
 * This servlet is responsible for getting the user email and deleting all records associated with that user.
 *
 * <p>
 * <strong>Dependencies:</strong>
 * <ul>
 * <li>Data_access_Objects.user_dao</li>
 * <li>java.io.IOException</li>
 * <li>java.io.PrintWriter</li>
 * <li>java.sql.SQLException</li>
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
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
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

public class delete_user_servelet extends HttpServlet {

    private user_dao userDAO;

    /**
     * Handles the POST request for deleting a user and all associated records.
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
        JSONObject jsonResponse = new JSONObject();

        String user_to_act_email = request.getParameter("user_to_act_email");

        try {
            userDAO = new user_dao();
            userDAO.delete_user(user_to_act_email);
            jsonResponse.put("message", "User got deleted successfully.");
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (SQLException ex) {
            try {
                jsonResponse.put("error", "An error occurred while deleting the user.");
            } catch (JSONException ex1) {
                Logger.getLogger(delete_user_servelet.class.getName()).log(Level.SEVERE, null, ex1);
            }
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Logger.getLogger(delete_user_servelet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            try {
                jsonResponse.put("error", "An error occurred while creating the JSON response.");
            } catch (JSONException ex1) {
                Logger.getLogger(delete_user_servelet.class.getName()).log(Level.SEVERE, null, ex1);
            }
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Logger.getLogger(delete_user_servelet.class.getName()).log(Level.SEVERE, null, ex);
        }

        PrintWriter out = response.getWriter();
        out.print(jsonResponse.toString());
        out.flush();
    }
}
