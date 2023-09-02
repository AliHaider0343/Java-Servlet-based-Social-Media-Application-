/**
 *
 * This servlet is responsible for removing the friendship between the current user and a friend.
 *
 * <p>
 * <strong>Dependencies:</strong>
 * <ul>
 * <li>Data_access_Objects.user_dao</li>
 * <li>java.io.BufferedReader</li>
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
import java.io.BufferedReader;
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

public class remove_friend_servlet extends HttpServlet {

    private user_dao userDAO;

    /**
     * Handles the POST request for removing a friendship between the current
     * user and a friend.
     *
     * @param request the servlet request
     * @param response the servlet response
     * @throws ServletException if an error occurs during the servlet execution
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        String friendEmail = "";
        String current_user_email = "";
        friendEmail = request.getParameter("friend_email");
        current_user_email = request.getParameter("current_user_email");
        response.setContentType("application/json");
        JSONObject jsonResponse = new JSONObject();

        userDAO = new user_dao();
        user user_obj = (user) session.getAttribute("user");
        StringBuilder sb = new StringBuilder();
        BufferedReader br = request.getReader();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        try {
            JSONObject jsonObj = new JSONObject(sb.toString());
            friendEmail = jsonObj.getString("friend_email");
            current_user_email = jsonObj.getString("current_user_email");

        } catch (Exception ex) {

        }
        if (friendEmail.isEmpty() && current_user_email.isEmpty()) {
            friendEmail = request.getParameter("friend_email");
            current_user_email = request.getParameter("current_user_email");

        }

        try {
            if (userDAO.RemoveFriend(current_user_email, friendEmail) > 0) {
                jsonResponse.put("message", "Friend Got Deleted Successfully.");
            } else {
                jsonResponse.put("message", "Error Accured while Processing.");
            }
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().print(jsonResponse);
        } catch (SQLException ex) {
            Logger.getLogger(remove_friend_servlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(remove_friend_servlet.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
