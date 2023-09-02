/**
 *
 * This servlet is responsible for getting all the available users and rendering their details on a JSP page.
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
 * <li>java_beans.user</li>
 * <li>javax.servlet.ServletException</li>
 * <li>javax.servlet.http.HttpServlet</li>
 * <li>javax.servlet.http.HttpServletRequest</li>
 * <li>javax.servlet.http.HttpServletResponse</li>
 * <li>javax.servlet.http.HttpSession</li>
 * <li>org.json.JSONArray</li>
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class users_feed_servelet extends HttpServlet {

    private user_dao userDAO;

    /**
     * Handles the POST request for getting all the users and rendering their
     * details.
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
        HttpSession session = request.getSession();

        JSONObject jsonResponse = new JSONObject();

        try {
            userDAO = new user_dao();
            ArrayList<user> users = userDAO.getAll_users();
            user user_obj = (user) session.getAttribute("user");
            JSONArray images = new JSONArray();
            JSONArray friend_ship_status = new JSONArray();

            String email = "";
            if (user_obj != null) {
                email = user_obj.getUser_email();
            } else {
                email = request.getParameter("email");
            }

            for (int i = 0; i < users.size(); i++) {
                Blob imageBlob = users.get(i).getProfile_pic();
                byte[] imageBytes = imageBlob.getBytes(1, (int) imageBlob.length());
                String imageBase64 = Base64.getEncoder().encodeToString(imageBytes);
                images.put(imageBase64);
                friend_ship_status.put(userDAO.isFriend(email, users.get(i).getUser_email()));
            }
            jsonResponse.put("status", "success");
            jsonResponse.put("images", images);
            jsonResponse.put("friendShip_status", friend_ship_status);
            if (user_obj != null) {
                jsonResponse.put("current_user", new JSONObject(user_obj));
            }
            jsonResponse.put("all_users", new JSONArray(users));

        } catch (SQLException ex) {
            try {
                jsonResponse.put("status", "error");
                jsonResponse.put("message", "An error occurred while fetching data.");
                Logger.getLogger(home_servelet.class.getName()).log(Level.SEVERE, null, ex);
            } catch (JSONException ex1) {
                Logger.getLogger(users_feed_servelet.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } catch (JSONException ex) {
            Logger.getLogger(users_feed_servelet.class.getName()).log(Level.SEVERE, null, ex);
        }

        out.print(jsonResponse.toString());
        out.flush();
    }

}
