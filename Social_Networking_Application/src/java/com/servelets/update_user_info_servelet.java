/**
 *
 * This servlet is responsible for fetching user information that is going to be updated.
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

public class update_user_info_servelet extends HttpServlet {

    private user_dao userDAO;

    /**
     * Handles the POST request for fetching user information to be updated.
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

        if (session == null || session.getAttribute("loggedIn") == null) {
            out.println("<script>alert('Please Login First and then Access Authorized Pages .')</script>");
            request.getRequestDispatcher("login.html").include(request, response);
        } else {
            try {
                user user_obj = (user) session.getAttribute("user");

                if (user_obj != null) {
                    userDAO = new user_dao();
                    user user_to_update = userDAO.get_single_user_info(request.getParameter("user_to_act_email"));
                    request.setAttribute("current_user", user_obj);
                    request.setAttribute("user_to_update", user_to_update);
                }
                request.getRequestDispatcher("update_user_by_admin.jsp").forward(request, response);
            } catch (SQLException ex) {
                Logger.getLogger(home_servelet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
