/**
 * This servlet handles the addition of an address for a user. It receives the address
 * details from the request parameters, associates it with the user, and updates the
 * user's address in the database.
 * <p>
 * <strong>Dependencies:</strong>
 * <ul>
 * <li>Data_access_Objects.user_dao</li>
 * <li>com.google.gson.Gson</li>
 * <li>com.google.gson.GsonBuilder</li>
 * <li>java.io.IOException</li>
 * <li>java.io.PrintWriter</li>
 * <li>java.sql.SQLException</li>
 * <li>java.util.HashMap</li>
 * <li>java.util.Map</li>
 * <li>java.util.logging.Level</li>
 * <li>java.util.logging.Logger</li>
 * <li>java_beans.Address</li>
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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java_beans.Address;
import java_beans.user;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class addAddressServlet extends HttpServlet {

    private user_dao userDAO;

    /**
     * Handles the POST request for adding a user address.
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

        try {
            HttpSession session = request.getSession();
            user user_obj = (user) session.getAttribute("user");
            String street = request.getParameter("street");
            String town = request.getParameter("town");
            String state = request.getParameter("state");
            String country = request.getParameter("country");
            String email = request.getParameter("email");
            Address user_address = new Address();
            user_address.setStreet(street);
            user_address.setTown(town);
            user_address.setState(state);
            user_address.setCountry(country);
            userDAO = new user_dao();
            Map<String, Object> jsonResponse = new HashMap<>();
            Gson gson = new GsonBuilder().create();
            if (userDAO.addUserAddress(email, user_address) > 0) {
                jsonResponse.put("success", true);
                jsonResponse.put("message", "Your Address Details has been Updated Successfully.");
            } else {
                jsonResponse.put("success", false);
                jsonResponse.put("message", "An Error Accured While Processing.");
            }
            String json = gson.toJson(jsonResponse);
            request.setAttribute("current_user", user_obj);
            out.print(json);
            out.flush();

        } catch (SQLException ex) {
            Logger.getLogger(addAddressServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
