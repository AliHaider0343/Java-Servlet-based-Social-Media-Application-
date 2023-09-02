/**
 *
 * This servlet handles the logout functionality by invalidating the current session and redirecting the user to the login page.
 *
 * @author Jose Omar
 * @version 1.0
 * @since 2023-05-14
 */
package com.servelets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LogoutServlet extends HttpServlet {

    /**
     * Handles the GET request for logging out.
     *
     * @param request the servlet request
     * @param response the servlet response
     * @throws ServletException if an error occurs during the servlet execution
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get the current session and invalidate it
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();
        }

        out.println("<script>alert('You are Being Logged out .')</script>");
        // Redirect the user to the login page

        request.getRequestDispatcher("login.jsp").include(request, response);
    }
}
