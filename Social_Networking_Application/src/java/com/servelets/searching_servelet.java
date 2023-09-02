/**
 *
 * This servlet is responsible for searching users based on location and gender.
 *
 * <p>
 * <strong>Dependencies:</strong>
 * <ul>
 * <li>Data_access_Objects.user_dao</li>
 * <li>java.io.BufferedReader</li>
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
import java.io.BufferedReader;
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

public class searching_servelet extends HttpServlet {

    private user_dao userDAO;

    /**
     * Handles the POST request for searching users based on location and
     * gender.
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
        String current_user_email = "";
        String query = "";

        query = request.getParameter("location");
        current_user_email = request.getParameter("current_user_email");

        response.setContentType("application/json");
        JSONObject jsonResponse = new JSONObject();
        try {
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
                query = jsonObj.getString("location");
                current_user_email = jsonObj.getString("current_user_email");

            } catch (Exception ex) {

            }
            if (query.isEmpty() && current_user_email.isEmpty()) {
                query = request.getParameter("location");
                current_user_email = request.getParameter("current_user_email");

            }

            userDAO = new user_dao();
            ArrayList<user> users = userDAO.getAll_users();
            JSONArray images = new JSONArray();
            JSONArray friend_ship_status = new JSONArray();
            JSONArray location_status = new JSONArray();

            for (int i = 0; i < users.size(); i++) {
                Blob imageBlob = users.get(i).getProfile_pic();
                byte[] imageBytes = imageBlob.getBytes(1, (int) imageBlob.length());
                String imageBase64 = Base64.getEncoder().encodeToString(imageBytes);
                images.put(imageBase64);
                friend_ship_status.put(userDAO.isFriend(current_user_email, users.get(i).getUser_email()));
                location_status.put(userDAO.isfrom_given_location_or_gender(query, users.get(i).getUser_email()));
            }
            jsonResponse.put("status", "success");
            jsonResponse.put("images", images);
            jsonResponse.put("friendShip_status", friend_ship_status);
            jsonResponse.put("location_status", location_status);
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
            Logger.getLogger(searching_servelet.class.getName()).log(Level.SEVERE, null, ex);
        }
        out.print(jsonResponse.toString());
        System.out.println(jsonResponse.toString());
        out.flush();
    }
}
